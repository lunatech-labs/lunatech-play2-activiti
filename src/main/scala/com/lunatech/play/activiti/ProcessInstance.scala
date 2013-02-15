package com.lunatech.play.activiti

import scala.collection.JavaConverters.{ asScalaBufferConverter, mapAsScalaMapConverter }

/**
 * Scala wrapper for a process instance
 */
trait ProcessInstance {
  def id: String
  def variables: Map[String, AnyRef]
  def apply(variableName: String): AnyRef = variables(variableName)
  def apply[A](variableName: String, default: A) = variables.get(variableName).map(_.asInstanceOf[A]).getOrElse(default)
}



