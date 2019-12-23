package com.eric.actors.typed

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import com.eric.actors.typed.TestBehavior.{TestBehaviorCommand, TestCommand}
import com.eric.actors.typed.TestBehavior2.TestCommand2

object TestBehavior {
  trait TestBehaviorCommand
  final case object TestCommand extends TestBehaviorCommand
  def apply():Behavior[TestBehaviorCommand] = Behaviors.setup{context=>
    new TestBehavior(List.empty,context)
  }
}
class TestBehavior(list:List[Int],context:ActorContext[TestBehaviorCommand]) extends AbstractBehavior[TestBehaviorCommand](context){
  var list2 = List.empty[Int]
  override def onMessage(msg: TestBehaviorCommand): Behavior[TestBehaviorCommand] =msg match {
    case TestCommand =>
      // 参数不可变的状态 只有内部的可变变量才能够添加成功
      list.::(1)
      list2 = 1:: list2
      context.log.info(list.toString)
      context.log.info(s"list2:$list2")
      //Behaviors.same
      this

  }
}
object TestBehavior2{
  trait TestBehaviorCommand2
  final case object TestCommand2 extends TestBehaviorCommand2
  def apply():Behavior[TestBehaviorCommand2]= k(List.empty)
  def k(list:List[Int]):Behavior[TestBehaviorCommand2]=Behaviors.receive[TestBehaviorCommand2]{
    (context,message)=>
      message match {
        // 不可边的状态 返回时才添加上
        case TestCommand2 =>
          context.log.info(s"list3:$list")
          k(1::list)
      }

  }
}
object TestBehaviorMain{
  def main(args: Array[String]): Unit = {
    val systemBehavior=Behaviors.setup[Any]{context=>
      val test=context.spawn(TestBehavior(),"test")
      val test2 = context.spawn(TestBehavior2(),"test2")
      for(i <-0 to 10){
        context.log.info(i.toString)
        test ! TestCommand
        test2 ! TestCommand2
      }
      Behaviors.stopped
    }
    val system = ActorSystem(systemBehavior,"system")
  }
}
