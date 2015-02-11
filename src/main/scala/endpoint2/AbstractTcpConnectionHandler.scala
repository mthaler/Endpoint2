package endpoint2

import akka.actor.Actor
import akka.io.Tcp._
import akka.util.ByteString

abstract class AbstractTcpConnectionHandler[R](handler: AbstractTcpHandler[R]) extends Actor {

  def receive: Receive = {
    case CommandFailed(connect: Connect) =>
      handler.connectFailed(connect)
      context stop self
    case c @ Connected(remote, local) =>
      handler.connected(c)
      val connection = sender()
      connection ! Register(self)
      context become {
        case data: ByteString =>
          connection ! Write(data)
        case CommandFailed(w: Write) =>
          // O/S buffer was full
          println("Write failed: " + w)
        case Received(data) =>
          handler.newData(deserialize(data))
        case "close" =>
          connection ! Close
        case closed: ConnectionClosed =>
          handler.connectionClosed(closed)
          context stop self
      }
  }

  def deserialize(bytes: ByteString): R
}
