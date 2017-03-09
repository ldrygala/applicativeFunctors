import scala.language.higherKinds


object ApplicativeFunctors extends App {

  import Domain._

  def toPay(order: Order): Double = order.totalCost - order.voucher.discount

  val order = Order(1, 100, Voucher(10))

  println(toPay(order))
}
