import com.google.inject.AbstractModule
import services._

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[AmountService]).to(classOf[AmountServiceImpl])
    bind(classOf[FireStoreClient]).to(classOf[FireStoreClientImpl])
  }
}
