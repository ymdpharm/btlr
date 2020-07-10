package controllers

import javax.inject._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import repositories.AmountRepository
import types.SlackRequest

import scala.concurrent.ExecutionContext

@Singleton
class BtController @Inject()(
  val controllerComponents: ControllerComponents,
  config: Configuration,
  amount: AmountRepository
)(implicit val ec: ExecutionContext)
    extends BaseController
    with play.api.Logging {

  def test: Action[AnyContent] =
    Action { request: Request[AnyContent] =>
      info(request)
      Ok(parseResponse("hogege")).as(JSON)
    }

  // receive application/x-www-form-urlencoded
  type FormUrlEncoded = Map[String, Seq[String]]

  def postTest: Action[FormUrlEncoded] =
    Action(parse.formUrlEncoded) { request: Request[FormUrlEncoded] =>
      info(request)
      Ok(parseResponse(request.body.mkString(","))).as(JSON)
    }

  def check: Action[FormUrlEncoded] =
    Action(parse.formUrlEncoded).async { request: Request[FormUrlEncoded] =>
      info(request)
      amount
        .getAll(SlackRequest(request).channelId)
        .map(ans => Ok(parseResponse(ans)).as(JSON))
    }

  def charge: Action[FormUrlEncoded] =
    Action(parse.formUrlEncoded).async { request: Request[FormUrlEncoded] =>
      info(request)
      val req = SlackRequest(request)
      amount
        .store(req.channelId, req.userId, req.text.toInt)
        .map(ans => Ok(parseResponse(ans)).as(JSON))
    }

  def erase: Action[FormUrlEncoded] =
    Action(parse.formUrlEncoded).async { request: Request[FormUrlEncoded] =>
      info(request)
      amount
        .deleteAll(SlackRequest(request).channelId)
        .map(ans => Ok(parseResponse(ans)).as(JSON))
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
}
