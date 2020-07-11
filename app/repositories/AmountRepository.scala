package repositories

import javax.inject.{Inject, Singleton}

import scala.concurrent.ExecutionContext
import scala.util.{Success, Try}

trait AmountRepository {
  def check(channelId: String): Try[String]
  def charge(channelId: String, userId: String, amount: Int): Try[String]
  def erase(channelId: String): Try[String]
}

@Singleton
class AmountRepositoryImpl @Inject()(underlying: FireStoreClient)(implicit val ec: ExecutionContext)
    extends AmountRepository {

  override def check(channelId: String): Try[String] =
    for {
      amounts <- underlying.find(channelId)
      ans = show(amounts)
    } yield ans

  override def charge(channelId: String, userId: String, amount: Int): Try[String] =
    for {
      amounts <- underlying.find(channelId)
      bottom = amounts.values.min
      tobe = amounts.updatedWith(userId) {
        case Some(v) => Some(v + amount - bottom)
        case None => Some(amount) // add new user.
      }
      _ <- underlying.update(channelId, tobe)
    } yield s"Ok, charged ${amount}. :+1:"

  override def erase(channelId: String): Try[String] =
    for {
      amounts <- underlying.find(channelId)
      tobe = amounts.map { case (k, _) => (k, 0) }
      _ <- underlying.update(channelId, tobe)
    } yield "Ok, erased. :+1:"

  private def show(amounts: Map[String, Int]) = {
    val bottom = amounts.values.min
    amounts.foldLeft(":eyes: Current balance is ...\r ```") { (x, m) =>
      x + "\r" + s"<@${m._1}> => ${m._2 - bottom}"
    } + "```"
  }
}
