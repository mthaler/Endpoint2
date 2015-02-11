package endpoint2

object Endpoint {

  sealed abstract trait EndpointMessage[+S, +R]
  case class Send[S](item: S) extends EndpointMessage[S, Nothing]
  case class NewData[R](item: R) extends EndpointMessage[Nothing, R]

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
   * Handler that handles received data
   *
   * @return
   */
  def handler: TypedActorRef[EndpointMessage[S, R]]
}
