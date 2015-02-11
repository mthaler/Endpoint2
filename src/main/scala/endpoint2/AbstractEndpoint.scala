package endpoint2

import akka.actor.ActorRef

abstract class AbstractEndpoint[S, R](val name: String, val handler: ActorRef) extends Endpoint[S, R]