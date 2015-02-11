package endpoint2

import akka.actor.{ActorSystem, ActorRef, Props}
import akka.io.Tcp.Connect
import akka.io.{Tcp, IO}
import akka.util.ByteString
import java.net.InetSocketAddress

object Client {

  def props(remote: InetSocketAddress, replies: ActorRef) =
    Props(classOf[Client], remote, replies)

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Client")
    val client = new Client(new InetSocketAddress("127.0.0.1", 6000), new ClientHandler)(system)

    Thread.sleep(1000)

    client.send(ByteString("Hello"))
  }
}

class Client(remote: InetSocketAddress, handler: AbstractTcpHandler[ByteString])(implicit val system: ActorSystem) extends AbstractTcpEndpoint[ByteString, ByteString]("client", handler, remote) {

  val connectionHandler = system.actorOf(Props(new AbstractTcpConnectionHandler[ByteString](handler) {
    IO(Tcp) ! Connect(remote)

    override def deserialize(bytes: ByteString): ByteString = bytes
  }))

  override def serialize(item: ByteString): ByteString = item
}
