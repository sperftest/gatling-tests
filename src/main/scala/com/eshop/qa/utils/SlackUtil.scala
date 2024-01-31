package com.eshop.qa.utils

import com.slack.api.Slack
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.slack.api.methods.request.files.FilesUploadRequest
import com.slack.api.methods.response.chat.ChatPostMessageResponse
import com.slack.api.methods.response.files.FilesUploadResponse
import com.typesafe.scalalogging.StrictLogging
import org.apache.commons.io.FileUtils

import java.io.File
import java.util
import scala.io.Source

object SlackUtil extends StrictLogging {

  val slack = Slack.getInstance()
  val TOKEN = CryptoUtil.decrypt(Source.fromFile("src/main/resources/slack.txt").getLines().next())

  val CHANNEL = "#channel-for-gatling"
  val TEST_CHANNEL = "#atest_chanel_for_gatling"

  def postSummaryToSlack(attachment: String): ChatPostMessageResponse = {
    slack.methods(TOKEN).chatPostMessage(ChatPostMessageRequest
      .builder()
      .channel(TEST_CHANNEL)
      .attachmentsAsString(attachment)
      .build())
  }

  def uploadFileToTheThread(file: File, threadTs: String): FilesUploadResponse = {
    slack.methods(TOKEN).filesUpload(FilesUploadRequest
      .builder()
      .channels(util.Arrays.asList(TEST_CHANNEL))
      .threadTs(threadTs)
      .fileData(FileUtils.readFileToByteArray(file))
      .filename("simulation.zip")
      .title("Report")
      .build())
  }
}