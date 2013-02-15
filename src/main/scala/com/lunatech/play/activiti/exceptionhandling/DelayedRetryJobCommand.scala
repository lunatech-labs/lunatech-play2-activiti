package com.lunatech.play.activiti.exceptionhandling

import java.io.{ PrintWriter, StringWriter }
import java.util.UUID
import org.activiti.engine.impl.cfg.TransactionState
import org.activiti.engine.impl.context.Context
import org.activiti.engine.impl.interceptor.{ Command, CommandContext }
import org.activiti.engine.impl.jobexecutor.MessageAddedNotification
import org.activiti.engine.impl.persistence.entity.JobEntity
import org.joda.time.DateTime

/**
 * FailedJobCommand that retries a job.
 */
class DelayedRetryJobCommand(jobId: String, exception: Throwable) extends Command[Object] {

  override def execute(commandContext: CommandContext) = {
    val job: JobEntity = Context
      .getCommandContext()
      .getJobManager()
      .findJobById(jobId)

    if (job.getDuedate == null) {
      // Set the duedate to now, so we can display it a UI on a failed jobs page. It doesn't do anything else (I hope...)
      job.setDuedate(new DateTime().toDate)
    }

    // Retry the job and schedule it for a later time
    // The strategy is to lock with a non-existing owner for a while.
    job.setRetries(job.getRetries() - 1)
    if (job.getRetries() > 0) {
      job.setLockOwner(UUID.randomUUID().toString())
      val now = new DateTime()
      val retryDate = now.plusSeconds(30).toDate
      job.setLockExpirationTime(retryDate)
    } else {
      job.setLockOwner(null)
      job.setLockExpirationTime(null)
    }

    // We add the date to the exception message, so we have it somewhere...
    job.setExceptionMessage(new DateTime().toString() + " / " + exception.getMessage())
    job.setExceptionStacktrace(getExceptionStacktrace())

    val jobExecutor = Context.getProcessEngineConfiguration().getJobExecutor()
    val messageAddedNotification = new MessageAddedNotification(jobExecutor)
    val transactionContext = commandContext.getTransactionContext()
    transactionContext.addTransactionListener(TransactionState.COMMITTED, messageAddedNotification)

    null
  }

  private def getExceptionStacktrace() = {
    val stringWriter = new StringWriter()
    exception.printStackTrace(new PrintWriter(stringWriter))
    stringWriter.toString()
  }
}