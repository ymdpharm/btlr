package controllers

import javax.inject._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import services.AmountService
import types.{AmountException, SlackRequest}

import scala.concurrent.ExecutionContext

@Singleton
class BtController @Inject()(
    val controllerComponents: ControllerComponents,
    config: Configuration,
    amount: AmountService
)(implicit val ec: ExecutionContext)
    extends BaseController
    with play.api.Logging {

  // receive application/x-www-form-urlencoded
  type FormUrlEncoded = Map[String, Seq[String]]

  def test: Action[AnyContent] =
    Action { request: Request[AnyContent] =>
      info(request)
      amount
        .check("testChannel")
        .recover(AmountException.recoverToMessage)
        .map(ans => Ok(parseResponse(ans)).as(JSON))
        .get
    }

  def check: Action[FormUrlEncoded] =
    Action(parse.formUrlEncoded) { request: Request[FormUrlEncoded] =>
      info(request)
      amount
        .check(SlackRequest(request).channelId)
        .recover(AmountException.recoverToMessage)
        .map(ans => Ok(parseResponse(ans)).as(JSON))
        .get
    }

  def charge: Action[FormUrlEncoded] =
    Action(parse.formUrlEncoded) { request: Request[FormUrlEncoded] =>
      info(request)
      val req = SlackRequest(request)
      amount
        .charge(req.channelId, req.userId, extractVal(req.text))
        .recover(AmountException.recoverToMessage)
        .map(ans => Ok(parseResponse(ans)).as(JSON))
        .get
    }

  def erase: Action[FormUrlEncoded] =
    Action(parse.formUrlEncoded) { request: Request[FormUrlEncoded] =>
      info(request)
      amount
        .erase(SlackRequest(request).channelId)
        .recover(AmountException.recoverToMessage)
        .map(ans => Ok(parseResponse(ans)).as(JSON))
        .get
    }

  private def info[A](request: Request[A]): Unit =
    request.method match {
      case "GET" =>
        logger.info(request.headers.toMap.mkString(","))
      case "POST" =>
        logger.info(request.headers.toMap.mkString(","))
        logger.info(request.body.toString)
    }

  private def parseResponse(text: String): String = Json.stringify {
    Json.toJson(
      Map(
        "response_type" -> Json.toJson("in_channel"),
        "text" -> Json.toJson(text)
      )
    )
  }

  private def extractVal(text: String): Int =
    text.replaceAll("[^0-9]", "").toInt
}
