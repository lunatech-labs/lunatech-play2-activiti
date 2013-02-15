package com.lunatech.play.activiti
import org.joda.time.{ DateTime, Duration }
import play.api.Play
import scala.collection.JavaConverters._

trait Task { this: HasEngine =>
  def underlying: org.activiti.engine.task.Task
  val id = underlying.getId
  val created = new DateTime(underlying.getCreateTime)
  val status: TaskStatus

  lazy val variables = engine.getTaskService.getVariables(id).asScala

  def apply(variableName: String): AnyRef = variables(variableName)

  def apply[A](variableName: String, default: A) = variables.get(variableName).map(_.asInstanceOf[A]).getOrElse(default)

  def statusCutoff(orange: Duration, red: Duration, start: DateTime = created) = {
    val now = new DateTime
    if (now.isAfter(start plus red)) TaskStatus.ERROR
    else if (now.isAfter(start plus orange)) TaskStatus.WARNING
    else TaskStatus.OK
  }

}