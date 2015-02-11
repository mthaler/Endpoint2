package endpoint2

import akka.actor.{Actor, ActorRef}
import akka.io.Tcp._
import akka.util.ByteString

abstract class AbstractTcpEndpoint[S, R](name: String, handler: ActorRef) extends AbstractEndpoint[S, R](name, handler) with Actor {

  def receive: Receive = {
    case CommandFailed(_: Connect) =>
      handler ! "connect failed"
      context stop self
    case c @ Connected(remote, local) =>
      handler ! c
      val connection = sender()
      connection ! Register(self)
      context become {
        case data: ByteString =>
          connection ! Write(data)
        case CommandFailed(w: Write) =>
          // O/S buffer was full
          handler ! "write failed"
        case Received(data) =>
          handler ! data
        case "close" =>
          connection ! Close
        case _: ConnectionClosed =>
          handler ! "connection closed"
          context stop self
      }
  }
}