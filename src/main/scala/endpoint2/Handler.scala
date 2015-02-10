package endpoint2

import akka.actor.Actor

class Handler extends Actor {

  def receive: Receive = {
    case x => println(x)
  }
}
