package endpoint2

import akka.io.Tcp.{ConnectionClosed, Connected, Connect}

abstract class AbstractTcpHandler[R] extends Handler[R] {

  def connectFailed(connect: Connect): Unit = {
    println("Connect failed: " + connect)
  }

  def connected(connected: Connected): Unit = {
    println("Connected: " + connected)
  }

  def connectionClosed(connectionClosed: ConnectionClosed): Unit = {
    println("Connection closed: " + connectionClosed)
  }
}
