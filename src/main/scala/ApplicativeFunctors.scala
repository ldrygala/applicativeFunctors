import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.higherKinds
import scala.util.{Failure, Try}

object ApplicativeFunctors extends App {

  case class Voucher(discount: Double)

  case class Order(id: Int, totalCost: Double)

  case class Delivery(cost: Double)

  import cats._
  import cats.implicits._

  val afterDiscount = (order: Order, delivery: Delivery, voucher: Voucher) => {
    order.totalCost - voucher.discount + delivery.cost
  }

  def toPay[F[_] : Applicative](orderF: F[Order], delivery: Delivery, voucherF: F[Voucher]): F[Double] = {
    val deliveryVoucherFunction = orderF.map(afterDiscount.curried)

    deliveryVoucherFunction.ap(delivery.pure[F]).ap(voucherF) //Applicative[F].map3(orderF,delivery.pure,voucherF)(afterDiscount)
  }

  val voucher = Voucher(10)
  val delivery = Delivery(5)
  val order = Order(1, 100)


  println(toPay(Option(order), delivery, Option(voucher)))
  println(toPay(Try(order), delivery, Failure[Voucher](new Exception("Boom"))))
  println(toPay(Future.failed[Order](new Exception("Boom")), delivery, Future.successful(voucher)))
}
