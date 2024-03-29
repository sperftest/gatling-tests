package com.eshop.qa.utils

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import org.influxdb.{InfluxDB, InfluxDBFactory}

import java.time.LocalDateTime

object DbClient extends ConfigUtil {

  private val influxdbUrl: String =
    if(isJenkins){
      influxUrl
    }else{
      influxUrLocal
    }

  val influxDB: InfluxDB = InfluxDBFactory.connect(influxdbUrl, influxUsername, influxPassword)
  influxDB.setDatabase(database)

  val simulationName: String = simulationClass.split('.')
      .lastOption
      .getOrElse("")
      .toLowerCase()

  val buildNumber: Int =
    PropertyConfigurator.getProperty("BUILD_NUMBER", "0").toInt

  val metricWriter = new WriteMetricToInfluxDB(simulationName, measurementName)

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