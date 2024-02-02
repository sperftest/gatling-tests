#!/bin/bash

# Function to display example of using
display_usage() {
    echo "Usage: $0 <run_scala>"
    echo "Example: $0 true"
    echo "    or: $0 false"
    exit 1
}

# Check if at least one argument is provided
if [ $# -ne 1 ]; then
    display_usage
fi

# Run Gatling test
mvn gatling:test -Dgatling.simulationClass=com.eshop.qa.simulation.NewParameterizedSimulation

# Check the value of the parameter
if [ "$1" == "true" ]; then
    # Run Scala application
    mvn scala:run -DmainClass=com.eshop.qa.utils.SlackNotificationMessenger
elif [ "$1" != "false" ]; then
    echo "Invalid argument. Please use 'true' or 'false'."
    display_usage
fi