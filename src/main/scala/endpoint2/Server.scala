package endpoint2

import akka.actor.{ActorSystem, Actor, ActorRef, Props}
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import java.net.InetSocketAddress

object Server {
  def props(remote: InetSocketAddress) =
    Props(classOf[Server], remote)

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Server")
    val server = system.actorOf(props(new InetSocketAddress("127.0.0.1", 6000)))
  }
}

class Server(serverAddress: InetSocketAddress) extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, serverAddress)

  def receive = {
    case b @ Bound(localAddress) =>
      println("Bound " + localAddress)

    case CommandFailed(_: Bind) => context stop self

    case c @ Connected(remote, local) =>
      println("Connected " + remote + " to " + local)
      val handler = context.actorOf(Props[EchoHandler])
      val connection = sender()
      connection ! Register(handler)
  }

}

