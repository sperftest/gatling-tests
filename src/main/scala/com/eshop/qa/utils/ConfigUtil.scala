package com.eshop.qa.utils

import com.eshop.qa.utils.PropertyConfigurator.getProperty
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging

trait ConfigUtil extends StrictLogging{
  //access to conf file
  val conf = ConfigFactory.load().withFallback(ConfigFactory.parseResources("performance.conf"))

  //service data
  val baseUrl: String = conf.getString("url")

  //load info
  val simulationClass: String = getProperty("SIMULATION", conf.getString("simulation-class"))

  //load profiling
  val loadModel: String = getProperty("TYPE_OF_LOAD_MODEL", conf.getString("load-model"))
  val rampUpDurationSeconds: Int = getProperty("RAMP_DURATION", conf.getString("ramp-up-duration-seconds")).toInt
  val steadyStateDurationSeconds: Int = getProperty("STEADY_STATE_DURATION_SECONDS", conf.getString("steady-state-duration-seconds")).toInt
  val testDurationSeconds: Int = getProperty("TEST_DURATION", testDurationSeconds.toString).toInt
  val usersRatePerSecond: Int = getProperty("USERS_RATE_PER_SECOND", conf.getString("users-rate-per-second")).toInt
  val usersCount: Int = getProperty("USERS", usersCount.toString).toInt
  val concurrentUsersAmount: Int = getProperty("CONCURRENT_USERS_AMOUNT", conf.getString("concurrent-users-amount")).toInt
  val paceMilliseconds: Int = getProperty("PACE_MILLISECONDS", conf.getString("pace-milliseconds")).toInt

  //credentials

}