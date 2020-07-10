package repositories

import com.google.cloud.firestore.Firestore
import javax.inject.{Inject, Singleton}
import play.api.Configuration

trait FireStoreClient {
  def db: Firestore
  def find(channelId: String): String
  def update(channelId: String, userId: String, amount: Int): Unit
}

@Singleton
class FireStoreClientImpl @Inject()(config: Configuration) extends FireStoreClient {
  import com.google.auth.oauth2.GoogleCredentials
  import com.google.firebase.FirebaseApp
  import com.google.firebase.FirebaseOptions
  import com.google.firebase.cloud.FirestoreClient
  import java.io.FileInputStream
  import collection.JavaConverters._

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

  FirebaseApp.initializeApp(options)

  override def db: Firestore = FirestoreClient.getFirestore

  // WIP WIP WIP
  override def find(channelId: String): String =
    db.collection("channels")
      .get()
      .get()
      .getDocuments
      .asScala
      .toList
      .head // channelId に
      .getData
      .toString

  override def update(channelId: String, userId: String, amount: Int): Unit =
    ???
}
