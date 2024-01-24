package com.eshop.qa.simulation

import com.eshop.qa.BaseSimulation
import com.eshop.qa.scenario.{OrderCreationScenario, SlackMessageScenario}
import com.eshop.qa.utils.DbClient
import io.gatling.core.Predef._

class NewParameterizedSimulation extends BaseSimulation {

  private val asserts = Seq(
    global.responseTime.percentile3.lte(5000)
  )

  before {
    println("test duration: " + testDurationSeconds +
            ", ramp up: " + rampUpDurationSeconds +
            ", users: " + usersCount)
  }

  setUp(
    OrderCreationScenario().populationBuilder
      .andThen(SlackMessageScenario().populationBuilder)
  ).assertions(asserts)
    .maxDuration(testDurationSeconds)

  after {
    DbClient.buildInfoWriter(OrderCreationScenario().startTime)
  }

}