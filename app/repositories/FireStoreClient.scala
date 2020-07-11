package repositories

import com.google.cloud.firestore.Firestore
import javax.inject.{Inject, Singleton}
import play.api.Configuration

import scala.util.Try

trait FireStoreClient {
  def find(channelId: String): Try[Map[String, Int]]
  def update(channelId: String, amounts: Map[String, Int]): Try[Unit]
  def close: Unit
}
// close が要る？

@Singleton
class FireStoreClientImpl @Inject()(config: Configuration) extends FireStoreClient {
  import com.google.auth.oauth2.GoogleCredentials
  import com.google.firebase.FirebaseApp
  import com.google.firebase.FirebaseOptions
  import com.google.firebase.cloud.FirestoreClient
  import java.io.FileInputStream
  import collection.JavaConverters._

  FirebaseApp.initializeApp(options)

  // cloud run で動作するならば catch 不要．
  private lazy val credentials: GoogleCredentials =
    try { GoogleCredentials.getApplicationDefault } catch {
      case e =>
        GoogleCredentials.fromStream(
          new FileInputStream(
            "./credentials/kmp00-281812-6003b39c9ffc.json"
          )
        )
    }

  // project config を env から
  private lazy val options: FirebaseOptions = new FirebaseOptions.Builder()
    .setCredentials(credentials)
    .setProjectId(config.get[String]("backend.project"))
    .build

  private lazy val db: Firestore = FirestoreClient.getFirestore

  override def close: Unit = db.close()

  override def find(channelId: String): Try[Map[String, Int]] =
    Try {
      db.collection("channels")
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
      val doc = db.collection("channels").document(channelId)
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
