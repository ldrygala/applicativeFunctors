/**
  * Created by lukasz.drygala on 09/03/17.
  */
object Domain {

  case class Voucher(discount: Double)

  case class Order(id: Int, totalCost: Double, voucher: Voucher)

}
