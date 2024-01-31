#!/bin/bash

# Run Gatling test
mvn gatling:test -Dgatling.simulationClass=com.eshop.qa.simulation.NewParameterizedSimulation

# Run Scala application
mvn scala:run -DmainClass=com.eshop.qa.utils.SlackNotificationMessenger
