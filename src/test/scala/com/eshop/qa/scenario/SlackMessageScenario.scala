package com.eshop.qa.scenario

import com.eshop.qa.BaseSimulation
import com.eshop.qa.utils.SlackNotificator
import io.gatling.core.Predef._
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}

import java.time.LocalDateTime

case class SlackMessageScenario() extends BaseSimulation{
  var startTime: LocalDateTime = LocalDateTime.now()
  val scn: ScenarioBuilder =
      SlackNotificator.sendNotificationAboutFinish(startTime, LocalDateTime.now().plusSeconds(testDurationSeconds))
//      .inject(
//        atOnceUsers(1)
//      )
//      .protocols(SlackNotificator.getSlackHTTPProtocol))
  val populationBuilder: PopulationBuilder = scn
    .inject(atOnceUsers(1))
    .protocols(SlackNotificator.getSlackHTTPProtocol)
//  setUp(populationBuilder)
}