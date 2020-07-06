import com.google.inject.AbstractModule
import repositories._

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[AmountRepository]).to(classOf[AmountRepositoryImpl])
    bind(classOf[FireStoreClient]).to(classOf[FireStoreClientImpl])
  }
}
