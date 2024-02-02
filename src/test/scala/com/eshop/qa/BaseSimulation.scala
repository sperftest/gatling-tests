package com.eshop.qa

import com.eshop.qa.utils.{ConfigUtil, ScenarioInjector}
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import io.gatling.http.protocol.HttpProtocolBuilder

class BaseSimulation extends Simulation with ConfigUtil{

  protected val httpConf: HttpProtocolBuilder =
    if (isJenkins){
      http.baseUrl(baseUrl)
    } else {
      http.baseUrl(baseUrlLocal)
    }

  def setInjectionProfiling(scenario: ScenarioBuilder): PopulationBuilder = {
    if (loadModel.contains("open")){
      scenario.inject(ScenarioInjector.injectOpenModel(usersCount, rampUpDurationSeconds))
    } else if(loadModel.contains("close")){
      scenario.inject(ScenarioInjector.injectClosedModel(concurrentUsersAmount, rampUpDurationSeconds, steadyStateDurationSeconds))
    }else {
      logger.warn("Type of load model had not been chosen when simulation started. (By default included 'atOnceUsers(1)').")
      scenario.inject(atOnceUsers(1))
    }
  }
}