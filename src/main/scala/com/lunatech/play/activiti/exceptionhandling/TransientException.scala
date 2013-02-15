package com.lunatech.play.activiti.exceptionhandling

/**
 * Wrapper for exceptions that may disappear when they are retried.
 */
class TransientException(message: String) extends RuntimeException(message)