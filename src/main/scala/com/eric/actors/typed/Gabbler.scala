package com.eric.actors.typed


import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.eric.actors.typed.SessionBehavior.PostMessage

object Gabbler {
 import ChatRoom._
 sealed trait SessionEvent
 final case class SessionGranted(handle:ActorRef[PostMessage])extends SessionEvent
 final case class SessionDenied(reason:String) extends SessionEvent
 final case class MessagePosted(screenName:String,message:String) extends SessionEvent

 def apply(): Behavior[SessionEvent] = Behaviors.setup{
  context=>Behaviors.receiveMessage{
   case SessionGranted(handle)=>
      handle ! PostMessage("Hello World")
      Behaviors.same
   case MessagePosted(screenName,message)=>
      context.log.info(s"message has been posted by '{}':{}",screenName,message)
      Behaviors.stopped
  }
 }
}
