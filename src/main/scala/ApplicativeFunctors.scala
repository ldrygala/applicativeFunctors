import scala.concurrent.Future
import scala.language.higherKinds
import scala.util.Try
import scala.concurrent.ExecutionContext.Implicits.global

object ApplicativeFunctors extends App {

  case class Voucher(discount: Double)
  case class Order(id: Int, totalCost: Double, voucher: Voucher)

  import cats._
  import cats.implicits._

  def toPay[F[_] : Functor](orderF: F[Order]): F[Double] = orderF.map { order =>
    order.totalCost - order.voucher.discount
  }

  val order = Order(1, 100, Voucher(10))
  val optionOrder = Option(order)
  val tryOrder = Try(order)
  val futureOrder = Future.failed[Order](new Exception("Boom"))

  println(toPay(optionOrder))
  println(toPay(tryOrder))
  println(toPay(futureOrder))
}
