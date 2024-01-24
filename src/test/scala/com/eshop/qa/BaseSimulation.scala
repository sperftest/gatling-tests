package com.eshop.qa

import com.eshop.qa.utils.{ConfigUtil, ScenarioInjector}
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import com.eshop.qa.utils.PropertyConfigurator.getProperty
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

class BaseSimulation extends Simulation with ConfigUtil{
  protected val httpConf: HttpProtocolBuilder = http
    .baseUrl(baseUrl)

  def setInjectionProfiling(scenario: ScenarioBuilder): PopulationBuilder = {
    scenario.inject(ScenarioInjector.injectOpenModel(usersCount, rampUpDurationSeconds))
//    if (loadModel.contains("open")){
//      scenario.inject(ScenarioInjector.injectOpenModel(usersCount, rampUpDurationSeconds))
//    } else if(loadModel.contains("close")){
//      scenario.inject(ScenarioInjector.injectClosedModel(concurrentUsersAmount, rampUpDurationSeconds, steadyStateDurationSeconds))
//    }else {
//      logger.warn("Type of load model had not been chosen when simulation started")
//      scenario.inject(atOnceUsers(1))
//    }
  }
}