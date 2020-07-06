package controllers

import javax.inject._
import play.api._
import play.api.libs.json.JsValue
import play.api.mvc._

@Singleton
class BtController @Inject()(val controllerComponents: ControllerComponents,
                             config: Configuration)
    extends BaseController {

  def test = Action { implicit request: Request[AnyContent] =>
    Ok(config.get[String]("app.config.test"))
  }

  def postTest = Action(parse.json) { implicit request: Request[JsValue] =>
    Ok("Got " + (request.body \ "name").as[String])
  }
}
