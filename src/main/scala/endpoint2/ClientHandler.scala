package endpoint2

import akka.util.ByteString

class ClientHandler extends AbstractTcpHandler[ByteString] {

  override def newData(item: ByteString): Unit = {
    println(item)
  }
}
