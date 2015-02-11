package endpoint2

import akka.actor.Actor
import akka.io.Tcp._
import akka.util.ByteString
import endpoint2.Endpoint.{EndpointMessage, NewData}

abstract class AbstractTcpEndpoint[S, R](name: String, handler: TypedActorRef[EndpointMessage[S, R]]) extends AbstractEndpoint[S, R](name, handler) with Actor {

  def receive: Receive = {
    case CommandFailed(_: Connect) =>
      handler.actorRef ! "connect failed"
      context stop self
    case c @ Connected(remote, local) =>
      handler.actorRef ! c
      val connection = sender()
      connection ! Register(self)
      context become {
        case data: ByteString =>
          connection ! Write(data)
        case CommandFailed(w: Write) =>
          // O/S buffer was full
          handler.actorRef ! "write failed"
        case Received(data) =>
          handler.actorRef ! NewData[R](deserialize(data))
        case "close" =>
          connection ! Close
        case _: ConnectionClosed =>
          handler.actorRef ! "connection closed"
          context stop self
      }
  }

  def deserialize(bytes: ByteString): R
}