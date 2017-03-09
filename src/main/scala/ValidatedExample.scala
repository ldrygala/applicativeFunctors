import cats.Applicative
import cats.data.Validated.{Invalid, Valid}
import cats.data.{Validated, ValidatedNel}

/**
  * Created by lukasz.drygala on 09/03/17.
  */
object ValidatedExample extends App {

  case class User(login: String, password: String, age: Int)

  import cats.implicits._

  def validLogin(login: String) = {
    if (login.split(".").length == 2)
      login.validNel
    else
      "login should have firstName.secondName format".invalidNel
  }

  def validPassword(password: String) = {
    if (password.length > 5)
      password.validNel
    else
      "password should have more then 5 characters".invalidNel
  }

  def validAge(age: String) = {
    Validated.catchNonFatal(age.toInt)
      .leftMap(_ => "not a number")
      .ensure("to play you should have more then 10 years")(_ > 10)
      .toValidatedNel
  }

  println("Enter login: ")
  val login = scala.io.StdIn.readLine
  println("Enter password: ")
  val password = scala.io.StdIn.readLine
  println("Enter age: ")
  val age = scala.io.StdIn.readLine

  type V[A] = ValidatedNel[String, A]

  val validatedUser = Applicative[V].map3(validLogin(login), validPassword(password), validAge(age))(User)
//  val validatedUser = (validLogin(login) |@| validPassword(password) |@| validAge(age)).map(User)

  validatedUser match {
    case Valid(user) => println(s"created $user")
    case Invalid(errors) => println(s"please fix: ${errors.toList.mkString("\n", "\n", "\n")}")
  }

}
