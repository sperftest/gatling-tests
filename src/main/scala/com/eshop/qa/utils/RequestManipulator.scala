package com.eshop.qa.utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

//todo
//THIS OBJECT IS OUTDATED AND WILL BE REMOVED IN THE NEXT UPDATES
object RequestManipulator {
  def saveStatusCodeAndResponseBody(request: HttpRequestBuilder): HttpRequestBuilder = {
    request
      .check(status.saveAs("statusCode"))
      .check(bodyString.saveAs("responseBody"))
  }
}