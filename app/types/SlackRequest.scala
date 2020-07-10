package types

import play.api.mvc.Request

case class SlackRequest(
    token: String,
    teamId: String,
    teamDomain: String,
    channelId: String,
    channelName: String,
    userId: String,
    userName: String,
    command: String,
    text: String,
    responseUrl: String)

object SlackRequest {
  def apply(request: Request[Map[String, Seq[String]]]): SlackRequest = {
    val getFirst = (key: String) => request.body(key).head
    SlackRequest(
      token = getFirst("token"),
      teamId = getFirst("team_id"),
      teamDomain = getFirst("team_domain"),
      channelId = getFirst("channel_id"),
      channelName = getFirst("channel_name"),
      userId = getFirst("user_id"),
      userName = getFirst("user_name"),
      command = getFirst("command"),
      text = getFirst("text"),
      responseUrl = getFirst("response_url")
    )
  }
}
