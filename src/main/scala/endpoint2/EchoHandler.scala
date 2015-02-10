package endpoint2

import akka.actor.{ActorSystem, Actor, ActorRef, Props}
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import java.net.InetSocketAddress

class EchoHandler extends Actor {
  import Tcp._
  def receive = {
    case Received(data) => sender() ! Write(data)
    case PeerClosed => context stop self
  }
}


