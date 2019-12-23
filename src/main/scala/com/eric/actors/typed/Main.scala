package com.eric.actors.typed

import akka.actor.typed.ActorSystem

object Main {
  def main(args: Array[String]): Unit = {
    val system : ActorSystem[HelloWorldMain.Start]= ActorSystem(HelloWorldMain(),"hello")
    system ! HelloWorldMain.Start("world")
    system ! HelloWorldMain.Start("Akka")
  }
}
