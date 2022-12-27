# Java Weather Observations

## What is this repository

This repository was a test made for a company which the requisit were to simulate weather balloon reported data and to generated statistics from them.

## Features

* Language level and runtime JDK is Java 8

* REST API

    * An `WeatherBalloonController` to generate the files and statistics

## How to use this repository

### Prerequisites

* Globally install Java 8
* Globally install Maven

### Installation

1. Clone this repository.
2. Run the command "mvn install" on this repository's folder.
3. Run the command "mvn spring-boot:run" on this repository's folder in order to run the application.

### How to use
#### Generate report file

curl --request POST \
  --url http://localhost:8080/weatherBalloon/generateFile
  
#### Convert data

 * you can convert the generated data to an desired scale
 * Temperature scale values: CELSIUS, FAHRENHEIT, KELVIN
 * Distance scale values: KM, MILES, METERS

 curl --request POST \
  --url http://localhost:8080/weatherBalloon/convertData \
  --header 'Content-Type: application/json' \
  --data '{
      "distanceScale": "KM",
      "temperatureScale": "CELSIUS"
   }'
   
#### Retrive flight statistics

curl --request GET \
  --url http://localhost:8080/weatherBalloon/flightStatistics
