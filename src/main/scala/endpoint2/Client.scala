package endpoint2

import akka.actor.{ActorSystem, Actor, ActorRef, Props}
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

class Client(remote: InetSocketAddress, listener: ActorRef) extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Connect(remote)

  def receive = {
    case CommandFailed(_: Connect) =>
      listener ! "connect failed"
      context stop self

    case c @ Connected(remote, local) =>
      listener ! c
      val connection = sender()
      connection ! Register(self)
      context become {
        case data: ByteString =>
          connection ! Write(data)
        case CommandFailed(w: Write) =>
          // O/S buffer was full
          listener ! "write failed"
        case Received(data) =>
          listener ! data
        case "close" =>
          connection ! Close
        case _: ConnectionClosed =>
          listener ! "connection closed"
          context stop self
      }
  }
}
