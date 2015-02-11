package endpoint2

trait Handler[R] {

  def newData(item: R)
}
