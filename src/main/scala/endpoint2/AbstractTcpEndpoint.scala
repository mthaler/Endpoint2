package endpoint2

abstract class AbstractTcpEndpoint[S, R](name: String, handler: Handler[R]) extends AbstractEndpoint[S, R](name, handler)