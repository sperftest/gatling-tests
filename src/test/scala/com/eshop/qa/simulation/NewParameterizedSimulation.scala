package com.eshop.qa.simulation

import com.eshop.qa.BaseSimulation
import com.eshop.qa.scenario.{OrderCreationScenario, SlackMessageScenario}
import io.gatling.core.Predef._

class NewParameterizedSimulation extends BaseSimulation {

  private val asserts = Seq(
    global.responseTime.percentile3.lte(5000)
  )

  setUp(
    OrderCreationScenario().populationBuilder,
    SlackMessageScenario().populationBuilder
//  ).assertions(asserts)
  )
    .maxDuration(testDurationSeconds)
}