package com.lunatech.play.activiti

import scala.collection.JavaConverters._

/**
 * Scala wrapper for a historic process instance
 */
trait HistoricProcessInstance extends ProcessInstance { this: HasEngine =>

  def underlying: org.activiti.engine.history.HistoricProcessInstance

  lazy val id = underlying.getId

  lazy val variables = engine.getHistoryService.
    createHistoricVariableInstanceQuery().
    processInstanceId(id).
    list.asScala.map { historicVariableInstance =>
      historicVariableInstance.getVariableName -> historicVariableInstance.getValue
    }.toMap

}