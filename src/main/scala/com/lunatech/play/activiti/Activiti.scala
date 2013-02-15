package com.lunatech.play.activiti

import play.api._

/**
 * Public Activiti API
 *
 * The underlying process engine is provided by the Activiti Plugin.
 */
object Activiti {

  private def engine(implicit app: Application) = {
    app.plugin[ActivitiPlugin] match {
      case Some(plugin) => plugin.engine
      case None => throw new Exception("Activiti Plugin is not registered.")
    }
  }

  def processEngine(implicit app: Application) = engine
  def runtimeService(implicit app: Application) = engine.getRuntimeService
  def repositoryService(implicit app: Application) = engine.getRepositoryService
  def taskService(implicit app: Application) = engine.getTaskService
  def historyService(implicit app: Application) = engine.getHistoryService
  def managementService(implicit app: Application) = engine.getManagementService
}