package services

import javax.inject.{Inject, Singleton}

import scala.concurrent.ExecutionContext
import scala.util.Try

trait AmountService {
  def check(channelId: String): Try[String]
  def charge(channelId: String, userId: String, amount: Int): Try[String]
  def erase(channelId: String): Try[String]
}

@Singleton
class AmountServiceImpl @Inject()(underlying: FireStoreClient)(implicit val ec: ExecutionContext)
    extends AmountService {

  override def check(channelId: String): Try[String] =
    for {
      amounts <- underlying.find(channelId)
      ans = show(amounts)
    } yield ans

  override def charge(channelId: String, userId: String, amount: Int): Try[String] =
    for {
      amounts <- underlying.find(channelId)
      added = amounts.updatedWith(userId) {
        case Some(v) => Some(v + amount) // incr
        case None => Some(amount) // add new user.
      }
      bottom = amounts.values.min
      rebalanced = added.map {
        case (k, v) => (k, v - bottom) // fix zero
      }
      _ <- underlying.update(channelId, rebalanced)
    } yield s"Ok, charged ${amount}. :+1:"

  override def erase(channelId: String): Try[String] =
    for {
      amounts <- underlying.find(channelId)
      tobe = amounts.map { case (k, _) => (k, 0) }
      _ <- underlying.update(channelId, tobe)
    } yield "Ok, erased. :+1:"

  private def show(amounts: Map[String, Int]) = {
    amounts.foldLeft("Current balance is ...:eyes: \r ```") { (x, m) =>
      x + "\r" + s"<@${m._1}> => ${m._2}"
    } + "```"
  }
}
