package com.eric.actors.typed

import akka.NotUsed
import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors

object TestTerminate {
  def apply(): Behavior[String] = Behaviors.setup { context =>
    Behaviors.receiveMessage {
      case "terminate" =>
        context.log.info("子actor停止了")
        Behaviors.stopped
    }
  }
}

object Test {
  def apply(): Behavior[NotUsed] = Behaviors.setup { context =>
    val testTerminate = context.spawn(TestTerminate(), "testTerminate")
    context.watch(testTerminate)
    testTerminate ! "terminate"
    testTerminate ! "terminate"
    Behaviors.receiveSignal {
      case (_, Terminated(ref)) =>
        context.log.info(ref.path.name)
        context.log.info("我即将停止了")
        Behaviors.stopped
    }
  }
  def main(args: Array[String]): Unit = {
    val system = ActorSystem(Test(), "test")
  }
}
