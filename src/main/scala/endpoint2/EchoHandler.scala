package endpoint2

import akka.actor.Actor
import akka.io.Tcp.{Write, PeerClosed, Received}

class EchoHandler extends Actor {
  def receive = {
    case Received(data) =>
      println("EchoHandler received: " + data)
      sender() ! Write(data)
    case PeerClosed => context stop self
  }
}


