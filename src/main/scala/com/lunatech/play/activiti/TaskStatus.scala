package com.lunatech.play.activiti

sealed trait TaskStatus

object TaskStatus {
  case object OK extends TaskStatus
  case object WARNING extends TaskStatus
  case object ERROR extends TaskStatus
}