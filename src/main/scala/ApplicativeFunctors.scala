import scala.concurrent.Future
import scala.language.higherKinds
import scala.util.{Failure, Try}
import scala.concurrent.ExecutionContext.Implicits.global

object ApplicativeFunctors extends App {

  case class Voucher(discount: Double)

  case class Order(id: Int, totalCost: Double)

  import cats._
  import cats.implicits._

  val afterDiscount = (order: Order, voucher: Voucher) => {
    order.totalCost - voucher.discount
  }

  def toPay[F[_] : Functor](orderF: F[Order], voucherF: F[Voucher]): F[Double] = {
    val voucherFunction = orderF.map(afterDiscount.curried)
    voucherFunction.map { f =>
      voucherF.map { voucher =>
        f(voucher)
      }
    }
  }

  val voucher = Voucher(10)
  val order = Order(1, 100)

  println(toPay(Option(order), Option(voucher)))
  println(toPay(Try(order), Failure[Voucher](new Exception("Boom"))))
  println(toPay(Future.failed[Order](new Exception("Boom")), Future.successful(voucher)))
}
