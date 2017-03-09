import scala.language.higherKinds


object ApplicativeFunctors extends App {

  import Domain._

  def toPay(maybeOrder: Option[Order]): Option[Double] = maybeOrder.map { order =>
    order.totalCost - order.voucher.discount
  }

  val order = Order(1, 100, Voucher(10))

  println(toPay(Option(order)))
}
