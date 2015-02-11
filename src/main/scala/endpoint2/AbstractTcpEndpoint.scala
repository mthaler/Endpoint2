package endpoint2

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.io.{Tcp, IO}
import akka.io.Tcp._
import akka.util.ByteString
import akka.actor.ActorDSL._

abstract class AbstractTcpEndpoint[S, R](name: String, handler: AbstractTcpHandler[R], remote: InetSocketAddress)(implicit val system: ActorSystem) extends AbstractEndpoint[S, R](name, handler) {

  val h = actor(new Act {
    whenStarting { IO(Tcp) ! Connect(remote) }
    become {
      case CommandFailed(connect: Connect) =>
        handler.connectFailed(connect)
        context stop self
      case c @ Connected(remote, local) =>
        handler.connected(c)
        val connection = sender()
        connection ! Register(self)
        become {
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
  })

  def send(item: S): Unit = {
    h ! serialize(item)
  }

  def serialize(item: S): ByteString

  def deserialize(bytes: ByteString): R
}