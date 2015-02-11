package endpoint2

abstract class AbstractEndpoint[S, R](val name: String, val handler: Handler[R]) extends Endpoint[S, R]