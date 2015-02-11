package endpoint2

trait Endpoint[S, R] {

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
   */
  def send(item : S)

  /**
   * Handler that handles received data. Usually this is an actor but we also allow for other handlers
   *
   * @return
   */
  def handler: Handler[R]

  def dispose()
}
