package repositories

import javax.inject.{Inject, Singleton}

case class Amount(user: String, amount: Int)

trait AmountRepository {
  def getAll(channelId: String): String // Class 作る
  def store(channelId: String, userId: String, amount: String): Unit
  def deleteAll(channelId: String): Unit
}

@Singleton
class AmountRepositoryImpl @Inject()(underlying: FireStoreClient)
    extends AmountRepository {

  override def getAll(channelId: String): String = {
    underlying.find(channelId)
  }

  override def store(channelId: String, userId: String, amount: String): Unit =
    ???

  override def deleteAll(channelId: String): Unit = ???
}
