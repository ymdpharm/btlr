package services

import com.google.cloud.firestore.Firestore
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import types.AmountException

import scala.util.Try

trait FireStoreClient {
  def find(channelId: String): Try[Map[String, Int]]
  def update(channelId: String, amounts: Map[String, Int]): Try[Unit]
  def close: Unit
}

@Singleton
class FireStoreClientImpl @Inject()(config: Configuration) extends FireStoreClient {
  import com.google.auth.oauth2.GoogleCredentials
  import com.google.firebase.FirebaseApp
  import com.google.firebase.FirebaseOptions
  import com.google.firebase.cloud.FirestoreClient
  import collection.JavaConverters._

  private val credentials: GoogleCredentials = GoogleCredentials.getApplicationDefault
  private val options: FirebaseOptions = new FirebaseOptions.Builder()
    .setCredentials(credentials)
    .build

  FirebaseApp.initializeApp(options)
  private val db: Firestore = FirestoreClient.getFirestore

  override def close: Unit = db.close()

  override def find(channelId: String): Try[Map[String, Int]] =
    Try {
      db.collection(config.get[String]("backend.collection"))
        .document(channelId)
        .get()
        .get()
        .getData
        .asInstanceOf[java.util.HashMap[String, Long]]
        .asScala
        .map { case (k, v) => (k, v.toInt) }
        .toMap
    }.recover {
      case _: NullPointerException => throw new AmountException.NoDataException()
      case _: RuntimeException => throw new AmountException.BackendDBException()
    }

  override def update(channelId: String, amounts: Map[String, Int]): Try[Unit] =
    Try {
      val doc = db.collection(config.get[String]("backend.collection")).document(channelId)
      val data = new java.util.HashMap[String, Long]()
      amounts.foreach {
        case (k, v) => data.put(k, v)
      }
      doc.set(data).get() // Await
      ()
    }.recover {
      case _: RuntimeException => throw new AmountException.BackendDBException()
    }
}
