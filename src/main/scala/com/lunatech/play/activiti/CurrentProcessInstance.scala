package com.lunatech.play.activiti

import scala.collection.JavaConverters._

/**
 * Scala wrapper for a running process instance
 */
trait CurrentProcessInstance extends ProcessInstance { this: HasEngine =>

  def underlying: org.activiti.engine.runtime.ProcessInstance

  lazy val id = underlying.getId

  lazy val variables = engine.getRuntimeService.getVariables(id).asScala.toMap

}