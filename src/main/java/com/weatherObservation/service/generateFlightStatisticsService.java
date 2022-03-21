package com.weatherObservation.service;

import com.weatherObservation.FlightStatisticsMapper;
import com.weatherObservation.dto.response.GenerateFlightStatisticsResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class generateFlightStatisticsService {

    public GenerateFlightStatisticsResponse generateFlightStatistics() throws Exception {
        if(!FileUtilsService.checkIfExistFile("flyingData")){
            throw new Exception("Data file not found!");
        }

        Map<String, Double> resultsTemperature = new HashMap<>();
        Map<String, Integer> observationsEachObservatory = new HashMap<>();
        Double totalDistance = 0.0;
        String latPoints = "";

        List<String> fileLines = getFileData();
        for (String line: fileLines) {
            String[] content = line.split("\\|");
            if (!content[0].equals("id")) {
                observationsEachObservatory.put(content[4], observationsEachObservatory.get(content[4]) + 1);

                if (!latPoints.isEmpty()) {
                    totalDistance = calculateTotalDistance(latPoints, content[2], totalDistance);
                }
                latPoints = content[2];

                Double temperature = getConvertedTemperature(new Double(content[3]), content[4]);
                if (resultsTemperature.get("minTemperature") == null ||
                        temperature < resultsTemperature.get("minTemperature")) {
                    resultsTemperature.put("minTemperature", temperature);
                }
                if (resultsTemperature.get("maxTemperature") == null ||
                        temperature > resultsTemperature.get("maxTemperature")) {
                    resultsTemperature.put("maxTemperature", temperature);
                }
                resultsTemperature.put("totalTemperature",
                        resultsTemperature.get("totalTemperature") == null ? temperature :
                                resultsTemperature.get("totalTemperature") + temperature);

            }
        }

        return FlightStatisticsMapper.buildFlightStatistics(
                resultsTemperature.get("minTemperature"),
                resultsTemperature.get("maxTemperature"),
                resultsTemperature.get("meanTemperature") / fileLines.size() - 1,
                totalDistance,
                observationsEachObservatory);
    }

    private Double getConvertedTemperature(Double temperature, String observatory) {
        switch (observatory) {
            case "US":
                return ((temperature - 32) * 5) / 9;
            case "FR":
            case "All Others":
                return temperature - 273.15F;
        }
        return temperature;
    }

    private Double calculateTotalDistance(String lastPoints, String currentPoints, Double totalDistance) {
        String[] lastPointsSplited = lastPoints.split(",");
        Double x1 = new Double(lastPointsSplited[0]);
        Double x2 = new Double(lastPointsSplited[1]);

        String[] currentPointsSplited = currentPoints.split(",");
        Double y1 = new Double(currentPointsSplited[0]);
        Double y2 = new Double(currentPointsSplited[1]);

        return totalDistance + Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    private List<String> getFileData() throws IOException {
        File dataFile = new File("classpath:flyingData.txt");
        return Files.lines(dataFile.toPath()).collect(Collectors.toList());
    }

}
