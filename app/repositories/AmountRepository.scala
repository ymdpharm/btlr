package repositories

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

case class Amount(user: String, amount: Int)

trait AmountRepository {
  def getAll(channelId: String): Future[String]
  def store(channelId: String, userId: String, amount: Int): Future[String]
  def deleteAll(channelId: String): Future[String]
}

@Singleton
class AmountRepositoryImpl @Inject()(underlying: FireStoreClient)(implicit val ec: ExecutionContext)
    extends AmountRepository {

  override def getAll(channelId: String): Future[String] = Future {
    underlying.find(channelId)
  }

  override def store(channelId: String, userId: String, amount: Int): Future[String] = ???

  override def deleteAll(channelId: String): Future[String] = ???
}
