package endpoint2

object Endpoint {
  case class NewData[R](item: R)

}

trait Endpoint[S, R] {

  import Endpoint._

  /**
   * Name for this endpoint, used for debugging
   *
   * @return name of this endpoint
   */
  def name: String

  /**
   * Sends an item. This method will not block
   *
   * @param item item to send
   * @return
   */
  def send(item: S)

  /**
   * Handler that handles received data
   *
   * @return
   */
  def handler: TypedActorRef[NewData[R]]
}
