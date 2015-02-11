package endpoint2

import akka.actor.{ActorSystem, ActorRef, Props}
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import java.net.InetSocketAddress

object Client {

  def props(remote: InetSocketAddress, replies: ActorRef) =
    Props(classOf[Client], remote, replies)

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Client")
    val handler = system.actorOf(Props[ClientHandler])
    val client = system.actorOf(props(new InetSocketAddress("127.0.0.1", 6000), handler))

    Thread.sleep(1000)

    client ! ByteString("Hello")
  }
}

class Client(remote: InetSocketAddress, handler: ActorRef) extends AbstractTcpEndpoint[ByteString, ByteString]("client", handler) {

  import Tcp._
  import context.system

  IO(Tcp) ! Connect(remote)
}
