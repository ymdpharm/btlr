package repositories

sealed class AmountException(msg: String) extends Exception(msg)

object AmountException {
  class NoDataException extends AmountException("No Data Found.")
  class BackendDBException(msg: String) extends AmountException(msg) // dekai

  def recoverToMessage: PartialFunction[Throwable, String] = {
    case e: NoDataException => e.getMessage
    case e: BackendDBException => e.getMessage
  }
}
