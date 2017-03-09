import cats.data.Validated.{Invalid, Valid}
import cats.data.ValidatedNel

import scala.util.Try

/**
  * Created by lukasz.drygala on 09/03/17.
  */
object ValidatedExample extends App {

  case class User(login: String, password: String, age: Int)

  import cats.implicits._

  def validLogin(login: String) = {
    if (login.split(".").length == 2)
      login.asRight
    else
      "login should have firstName.secondName format".asLeft
  }

  def validPassword(password: String) = {
    if (password.length > 5)
      password.asRight
    else
      "password should have more then 5 characters".asLeft
  }

  def validAge(age: String) = {
    Either
      .fromTry(Try(age.toInt))
      .left.map(_ => "not a number")
      .flatMap(age => if (age > 10) age.asRight else "to play you should have more then 10 years".asLeft)
  }

  println("Enter login: ")
  val login = scala.io.StdIn.readLine
  println("Enter password: ")
  val password = scala.io.StdIn.readLine
  println("Enter age: ")
  val age = scala.io.StdIn.readLine

  type V[A] = ValidatedNel[String, A]

  //  val validatedUser = Applicative[V].map3(validLogin(login), validPassword(password), validAge(age))(User)
  //  val validatedUser = (validLogin(login) |@| validPassword(password) |@| validAge(age)).map(User)

  val validatedUser = for {
    l <- validLogin(login)
    p <- validPassword(password)
    a <- validAge(age)
  } yield User(l,p,a)

  println(validatedUser)

}
