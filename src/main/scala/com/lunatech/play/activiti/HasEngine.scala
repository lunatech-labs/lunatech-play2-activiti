package com.lunatech.play.activiti

import org.activiti.engine.ProcessEngine

/**
 * Trait providing an Activiti `ProcessEngine`
 */
trait HasEngine {
  def engine: ProcessEngine
}