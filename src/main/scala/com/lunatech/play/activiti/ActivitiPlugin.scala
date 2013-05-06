package com.lunatech.play.activiti

import scala.collection.JavaConverters.asScalaBufferConverter
import org.activiti.engine.{ ProcessEngine, ProcessEngineConfiguration }
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.activiti.engine.impl.history.HistoryLevel
import com.lunatech.play.activiti.db.SquerylJoinedTransactionFactory
import com.lunatech.play.activiti.exceptionhandling.RecoverableJobCommandFactory
import play.api.{ Application, Logger, Play, Plugin }
import play.api.db.DB

/**
 * Initializes Activiti and deploys all BPMN definitions in the conf/processes directory.
 */
class ActivitiPlugin(implicit app: Application) extends Plugin {

  lazy val engine: ProcessEngine = {
    val configuration = ProcessEngineConfiguration.
      createStandaloneProcessEngineConfiguration.asInstanceOf[ProcessEngineConfigurationImpl].
      setFailedJobCommandFactory(new RecoverableJobCommandFactory()).
      setProcessEngineName("Activiti Process Engine").
      setDataSource(DB.getDataSource()(app)).
      setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE).
      setJobExecutorActivate(true).
      setTransactionFactory(new SquerylJoinedTransactionFactory()).
      setHistory(HistoryLevel.ACTIVITY.getKey)

    addProjectConfiguration(configuration).buildProcessEngine()
  }

  /**
   * Start the Activiti Process Engine and load new process definitions.
   */
  override def onStart() {
    Logger.info("Starting Activiti")
    engine
    loadNewProcessDefinitions()
  }

  /**
   * Stop the Activiti Process Engine
   */
  override def onStop() {
    Logger.info("Stopping Activiti")
    engine.close()
  }

  private def loadNewProcessDefinitions() {
    Play.configuration.underlying.getList("processDefinitions").asScala.map { configValue =>
      val resourceName = configValue.unwrapped.asInstanceOf[String]
      Logger.info("Loading process definition %s" format resourceName)
      val deployment = engine.getRepositoryService
        .createDeployment()
        // Name must be different for each resource, otherwise 'enableDuplicateFiltering' breaks.
        .name(resourceName.takeWhile(_ != '.'))
        .addClasspathResource("processes/" + resourceName)
        .enableDuplicateFiltering() // Don't deploy if it's already deployed
        .deploy()
    }
  }

  /**
   * Lookup if the configuration file has a 'Configurator' defined, and use it to further configure the process engine.
   */
  private def addProjectConfiguration(processEngineConfiguration: ProcessEngineConfiguration) = {
    app.configuration.getString("activiti.configurator") map { className =>
      Logger.info("Trying to load class %s to configure Activiti" format className)
      val configuratorClass = Play.classloader.loadClass(className).asInstanceOf[Class[Configurator]]
      val configurator = configuratorClass.getConstructor(classOf[Application]).newInstance(app)
      configurator.configure(processEngineConfiguration)
    } getOrElse processEngineConfiguration
  }
}