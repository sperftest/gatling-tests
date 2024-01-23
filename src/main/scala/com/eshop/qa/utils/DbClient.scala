package com.eshop.qa.utils

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import org.influxdb.{InfluxDB, InfluxDBFactory}
import com.eshop.qa.utils.ConfigUtil

import java.time.LocalDateTime

object DbClient extends ConfigUtil {
  val url = "http://localhost:8653"
  val username = "admin"
  val password = "admin"
  val database = "graphite"

  val influxDB: InfluxDB = InfluxDBFactory.connect(url, username, password)
  influxDB.setDatabase(database)

  val simulationName: String = simulationClass.split('.')
      .lastOption
      .getOrElse("")
      .toLowerCase()

  val buildNumber: Int =
    PropertyConfigurator.getProperty("BUILD_NUMBER", "0").toInt

  val metricWriter = new WriteMetricToInfluxDB(simulationName)

  def writeMetricWriter(requestName: String): ChainBuilder = {
    doIf(session => {
      session("statusCode").as[Int] != 200
    }) {
      metricWriter.writeError(influxDB, requestName)
    }
  }

  def buildInfoWriter(startTime: LocalDateTime): Unit = {
    metricWriter.writeBuildInfo(influxDB, buildNumber, startTime)
  }

}