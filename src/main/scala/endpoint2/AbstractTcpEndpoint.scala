package endpoint2

import java.net.InetSocketAddress

import akka.actor.ActorRef
import akka.util.ByteString

abstract class AbstractTcpEndpoint[S, R](name: String, handler: AbstractTcpHandler[R], remote: InetSocketAddress) extends AbstractEndpoint[S, R](name, handler) {

  val connectionHandler: ActorRef

  def send(item: S): Unit = {
    connectionHandler ! serialize(item)
  }

  def serialize(item: S): ByteString
}