package com.eshop.qa.utils

import com.eshop.qa.utils.PropertyConfigurator.getProperty
import com.google.gson.JsonParser
import com.typesafe.scalalogging.StrictLogging
import org.apache.commons.io.FileUtils
import org.zeroturnaround.zip.ZipUtil

import java.io.File
import java.sql.Timestamp
import java.util.stream.IntStream
import scala.io.Source

object SlackNotificationMessenger extends StrictLogging with ConfigUtil {

  val attachmentTemplate = "[{\"color\": \"#008000\",\n" +
    "\"text\": \"```textHolder```\",\n" +
    "\"pretext\": \"*SimulationHolder* was ended. Duration: *DurationHolder* *TimeUnitsHolder*. The report zip will be attached to the thread. Unzip it and open *index.html* file\",\n" +
    "\"title\": \"Grafana link\",\n" +
    "\"title_link\": \"http://127.0.0.1:8857/d/gatling/gatling-report-metrics?from=now-5m&refresh=5s\"}]"

  val GATLING_REPORTS_DIR = "target/gatling"

  private def getReportFolder(simulationName: String, reportName: String = "latest") = {
    var gatlingReportsDir: File = null

    if (reportName.equals("latest"))
      gatlingReportsDir = new File(GATLING_REPORTS_DIR)
    else
      gatlingReportsDir = new File(GATLING_REPORTS_DIR + "/" + reportName)

    val simulationReports = gatlingReportsDir.listFiles()
      .filter(_.getName.split("-")(0).equals(simulationName))
    if (simulationReports.isEmpty) throw new NoSuchElementException("There is no " + simulationName + " in report folder")
    var newestReport = simulationReports(0)
    simulationReports.foreach(currentReport => {
      val newestTimestamp = new Timestamp(newestReport.getName.split("-")(1).toLong)
      val currentTimestamp = new Timestamp(currentReport.getName.split("-")(1).toLong)
      if (newestTimestamp.before(currentTimestamp)) {
        newestReport = currentReport
      }
    })
    logger.info(simulationName + " newest report -> " + newestReport.getName)
    newestReport
  }

  private def getSummaryFromJson(json: File) = {
    var stringJson: String = ""
    Source.fromFile(json).foreach(stringJson += _)
    val parserObject = JsonParser.parseString(stringJson).getAsJsonObject
    val globalStats = parserObject.get("stats").getAsJsonObject
    var text = "Response Time Distribution:\n"
    IntStream.range(1, 5).forEach(i => {
      val group = globalStats.get("group" + i).getAsJsonObject
      val groupName = group.get("name").getAsString
      val count = group.get("count").getAsInt
      val percentage = group.get("percentage").getAsInt
      var line = new StringBuilder(groupName).append("tabs").append(count).append(" (").append(percentage).append("%)").toString()
      if (i == 1) line = line.replaceAll("tabs", "             ")
      if (i == 2) line = line.replaceAll("tabs", "   ")
      if (i == 3) line = line.replaceAll("tabs", "            ")
      if (i == 4) line = line.replaceAll("tabs", "                 ")
      text += line + "\n"
    })
    text
  }

  def main(args: Array[String]): Unit = {
    val zippingPath = "target/report.zip"
    val simulationName = sys.props.getOrElse("simulationName", "NewParameterizedSimulation")
    val reportFolder = getReportFolder(simulationName.toLowerCase)
    FileUtils.copyDirectory(reportFolder, new File("target/report"))
    val copiedFolder = new File("target/report")
    copiedFolder.listFiles().filter(_.getName.contains("simulation.log")).head.delete()
    print(reportFolder.getName + " will be used for uploading\n")
    ZipUtil.pack(copiedFolder, new File(zippingPath))
    FileUtils.deleteDirectory(copiedFolder)
    val statsJson = reportFolder
      .listFiles().filter(_.getName.equals("js")).head
      .listFiles().filter(_.getName.equals("stats.json")).head
    val attachment = attachmentTemplate
      .replaceAll("textHolder", getSummaryFromJson(statsJson))
      .replaceAll("SimulationHolder", simulationName)
      .replaceAll("DurationHolder", getProperty("TEST_DURATION", testDurationSeconds.toString))
      .replaceAll("TimeUnitsHolder", getProperty("DURATION_MEASUREMENTS", "seconds"))

    val threadTs = SlackUtil.postSummaryToSlack(attachment).getTs
    SlackUtil.uploadFileToTheThread(new File(zippingPath), threadTs)
  }
}