package com.lunatech.play.activiti.exceptionhandling

import org.activiti.engine.impl.jobexecutor.FailedJobCommandFactory

/**
 * Custom FailedJobCommandFactory that retries failed jobs when
 * the exception is a [[TransientException]].
 */
class RecoverableJobCommandFactory extends FailedJobCommandFactory {

  override def getCommand(jobId: String, exception: Throwable) = exception match {
    case e: TransientException => new DelayedRetryJobCommand(jobId, exception)
    case _ => new NoRetryJobCommand(jobId, exception);
  }

}