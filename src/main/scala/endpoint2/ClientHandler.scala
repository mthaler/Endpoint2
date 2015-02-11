package endpoint2

import akka.actor.Actor
import akka.io.Tcp.Connected
import akka.util.ByteString

class ClientHandler extends Actor {

  def receive: Receive = {
    case Connected(remote, local) => println("Handler: connected " + remote + " to " + local)
    case data: ByteString => println("Handler: received " + data)
    case x => println(x)
  }
}
