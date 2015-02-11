package endpoint2

import akka.actor.ActorRef

trait Endpoint[S, R] {

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
  def handler: ActorRef
}
