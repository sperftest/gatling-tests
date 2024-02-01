package com.eshop.qa.utils

import com.eshop.qa.utils.PropertyConfigurator.getProperty
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import java.time.format.DateTimeFormatter
import java.time.{Duration, LocalDateTime}

object SlackNotificator {

  private val grafanaLink = "127.0.0.1:3000/d/gatling/gatling-report-metrics?from=now-5m&refresh=5s"
  private val slackDomain = "https://hooks.slack.com/"
  private val channelId = CryptoUtil.decrypt("O4vLdAXtrJF6J5XPOWBqt0ZZIdImZlN2EtIWmrjZhZAfRdYjBv7SeEcL/6Up6AMq9Yi1u6JYXujVZb/TLOzOqeMuIbrnkCH9rp9hkZPBneU=")

  private def requestBody(authorName: String, startDateAndTime: LocalDateTime, endDateAndTime: LocalDateTime): String ={
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formattedDateTime: String = startDateAndTime.format(formatter)

    var testDuration = Duration.between(startDateAndTime, endDateAndTime)
    val hours: Long = testDuration.toHours
    testDuration = testDuration.minusHours(hours)
    val minutes: Long = testDuration.toMinutes
    testDuration = testDuration.minusMinutes(minutes)
    val seconds: Long = testDuration.getSeconds

    val body =
      s"""{
         |"grafanaLink": "$grafanaLink",
         |"authorName": "$authorName",
         |"startDateAndTime": "$formattedDateTime",
         |"testDuration": "$hours h $minutes m $seconds s"}"""
        .stripMargin
    println(body)
    body
  }

  def sendNotificationAboutFinish(startTime: LocalDateTime, endTime: LocalDateTime): ScenarioBuilder = {
    scenario("Finish test notification")
      .exec(http("Slack Webhook")
        .post(channelId) // Append your actual webhook path
        .body(
          StringBody(
            requestBody(
              getProperty("JENKINS_LOGIN", "LOGIN_NOT_FROM_JENKINS"),
              startTime,
              endTime
            )
          )
        )
        .header("Content-Type", "application/json")
        .check(status.is(200))
      )
  }

  def getSlackHTTPProtocol: HttpProtocolBuilder = {
    val slackProtocol: HttpProtocolBuilder = http.baseUrl(slackDomain)
    slackProtocol
  }
}