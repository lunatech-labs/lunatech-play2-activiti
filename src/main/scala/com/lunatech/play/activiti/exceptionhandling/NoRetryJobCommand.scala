package com.lunatech.play.activiti.exceptionhandling

import java.io.{ PrintWriter, StringWriter }
import org.activiti.engine.impl.cfg.TransactionState
import org.activiti.engine.impl.context.Context
import org.activiti.engine.impl.interceptor.{ Command, CommandContext }
import org.activiti.engine.impl.jobexecutor.JobAddedNotification
import org.activiti.engine.impl.persistence.entity.JobEntity
import org.joda.time.DateTime

/**
 * FailedJobCommand that doesn't retry the job.
 */
class NoRetryJobCommand(jobId: String, exception: Throwable) extends Command[Object] {

  override def execute(commandContext: CommandContext) = {
    val job: JobEntity = Context
      .getCommandContext()
      .getJobEntityManager()
      .findJobById(jobId)

    job.setRetries(0)
    job.setLockOwner(null)
    job.setLockExpirationTime(null)

    // We add the date to the exception message, so we have it somewhere...
    job.setExceptionMessage(new DateTime().toString() + " / " + exception.getMessage())
    job.setExceptionStacktrace(getExceptionStacktrace())

    val jobExecutor = Context.getProcessEngineConfiguration().getJobExecutor()
    val messageAddedNotification = new JobAddedNotification(jobExecutor)
    val transactionContext = commandContext.getTransactionContext()
    transactionContext.addTransactionListener(TransactionState.COMMITTED, messageAddedNotification)

    Unit
  }

  private def getExceptionStacktrace() = {
    val stringWriter = new StringWriter()
    exception.printStackTrace(new PrintWriter(stringWriter))
    stringWriter.toString()
  }
}