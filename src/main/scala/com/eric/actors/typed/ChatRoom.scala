package com.eric.actors.typed

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.eric.actors.typed.Gabbler.{
  MessagePosted,
  SessionEvent,
  SessionGranted
}
import com.eric.actors.typed.SessionBehavior.{
  NotifyClient,
  PostMessage,
  SessionCommand
}
object ChatRoom {
  sealed trait RoomCommand
  final case class GetSession(screenName: String,
                              replyTo: ActorRef[SessionEvent])
      extends RoomCommand
  private final case class PublishSessionMessage(screenName: String,
                                                 message: String)
      extends RoomCommand
  def apply(): Behavior[RoomCommand] = chatRoom(List.empty)

  private def chatRoom(
      sessions: List[ActorRef[SessionCommand]]): Behavior[RoomCommand] = {
    Behaviors.receive { (context, message) =>
      message match {
        case GetSession(screenName, client) =>
          // create a child actor for further interaction with the client
          val ses = context.spawn(
            session(context.self, screenName, client),
            name = URLEncoder.encode(screenName, StandardCharsets.UTF_8.name))
          client ! SessionGranted(ses)
          chatRoom(ses :: sessions)
        case PublishSessionMessage(screenName, message) =>
          val notification = NotifyClient(MessagePosted(screenName, message))
          sessions.foreach(_ ! notification)
          Behaviors.same
      }

    }
  }
  private def session(
      room: ActorRef[PublishSessionMessage],
      screenName: String,
      client: ActorRef[SessionEvent]): Behavior[SessionCommand] =
    Behaviors.receiveMessage {
      case PostMessage(message) =>
        room ! PublishSessionMessage(screenName, message)
        Behaviors.same
      case NotifyClient(message) =>
        client ! message
        Behaviors.same
    }
}
