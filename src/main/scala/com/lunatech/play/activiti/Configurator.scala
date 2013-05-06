package com.lunatech.play.activiti

import org.activiti.engine.ProcessEngineConfiguration

trait Configurator {
  def configure(configuration: ProcessEngineConfiguration): ProcessEngineConfiguration
}