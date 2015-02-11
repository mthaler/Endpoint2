package endpoint2

import endpoint2.Endpoint.EndpointMessage

abstract class AbstractEndpoint[S, R](val name: String, val handler: TypedActorRef[EndpointMessage[S, R]]) extends Endpoint[S, R]