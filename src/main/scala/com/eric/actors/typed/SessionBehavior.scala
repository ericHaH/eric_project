package com.eric.actors.typed

import com.eric.actors.typed.Gabbler.MessagePosted

object SessionBehavior {
 trait SessionCommand
 final case class PostMessage(message:String) extends SessionCommand
 final case class NotifyClient(message:MessagePosted) extends SessionCommand

}
