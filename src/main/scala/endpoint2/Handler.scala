package endpoint2

trait Handler[R] {

  /**
   * Called if new data is received
   *
   * @param item received item
   */
  def newData(item: R)
}
