package endpoint2

import endpoint2.Endpoint.NewData

abstract class AbstractEndpoint[S, R](val name: String, val handler: TypedActorRef[NewData[R]]) extends Endpoint[S, R]