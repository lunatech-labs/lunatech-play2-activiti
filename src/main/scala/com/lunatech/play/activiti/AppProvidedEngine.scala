package com.lunatech.play.activiti

import play.api.Play.current

/**
 * Engine provided by the 'current' Play application plugin.
 *
 * The Play application is the one from `play.api.Play.current`
 */
trait AppProvidedEngine extends HasEngine {
  override def engine = Activiti.processEngine
}