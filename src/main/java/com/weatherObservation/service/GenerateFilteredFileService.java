package com.weatherObservation.service;

import com.weatherObservation.dto.request.GenerateFilteredFileRequest;
import com.weatherObservation.entity.Distance;
import com.weatherObservation.entity.Temperature;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenerateFilteredFileService {

    public void generateFilteredFile(GenerateFilteredFileRequest request) throws Exception{
        if(!FileUtilsService.checkIfExistFile("flyingData")){
            throw new Exception("Data file not found!");
        }
        FileUtilsService.deleteExitingFile("flyingDataFiltered");
        File file = new File("classpath:/flyingDataFiltered.txt");
        file.createNewFile();
        buildFile(file, request);

    }

    private void buildFileHeader(StringBuilder content) {
        content.append("id|timestamp|location|temperature|observatory|distanceFromLastObservation\n");
    }

    private void buildFile(File file, GenerateFilteredFileRequest request) throws IOException {
        StringBuilder content = new StringBuilder();
        String lastKnownLocation = "";
        buildFileHeader(content);
        FileUtilsService.writeInFile(file, content.toString());

        for (String line : getFileData()) {
            content.setLength(0);
            Double distanciaFromLastPoin = 0.0;
            String[] fileContent = line.split("\\|");

            if (!fileContent[0].equals("id")) {
                if (!lastKnownLocation.isEmpty())
                    distanciaFromLastPoin = calculateDistanceFromLastPoint(lastKnownLocation, fileContent[2]);

                lastKnownLocation = fileContent[2];
                content.append(buildConvertedFileContent(fileContent, request, distanciaFromLastPoin));
                FileUtilsService.writeInFile(file, content.toString());
            }

        }
    }

    private String buildConvertedFileContent(
            String[] fileContent, GenerateFilteredFileRequest request, Double distanciaFromLastPoin) {

        StringBuilder line = new StringBuilder();
        Double temperature = new Double(fileContent[3]);
        String observatory = fileContent[4];

        line.append(new Integer(fileContent[0])).append("|");
        line.append(fileContent[1]).append("|");
        line.append(fileContent[2]).append("|");
        line.append(formatNumber(getTemperature(temperature, request.getTemperatureScale(), observatory))).append("|");
        line.append(observatory).append("|");
        line.append(formatNumber(convertDistanceFromLastPoint(distanciaFromLastPoin, request.getDistanceScale())));
        return line.append("\n").toString();
    }

    private String formatNumber(Double number) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(number);
    }

    private Double calculateDistanceFromLastPoint(String lastKnownLocation, String currentLocation) {
        String[] lastPoints = lastKnownLocation.split(",");
        Double x1 = new Double(lastPoints[0]);
        Double x2 = new Double(lastPoints[1]);

        String[] currentPoints = currentLocation.split(",");
        Double y1 = new Double(currentPoints[0]);
        Double y2 = new Double(currentPoints[1]);

       return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    private Double getTemperature(Double temperature, Temperature scale, String observatory) {
        switch (scale) {
            case CELSIUS:
                return getCelsiusTemperature(observatory, temperature);
            case FAHRENHEIT:
                return getFahrenheitTemperature(observatory, temperature);
            case KELVIN:
                return getKelvinTemperature(observatory, temperature);
        }
        return temperature;
    }

    private Double getKelvinTemperature(String observatory, Double temperature) {
        switch (observatory) {
            case "US":
                return 273.5 + ((temperature - 32.0) * (5.0 / 9.0));
            case "AU":
                return temperature + 273.15;
        }
        return temperature;
    }

    private Double getFahrenheitTemperature(String observatory, Double temperature) {
        switch (observatory) {
            case "AU":
                return (9 / 5) * temperature + 32;
            case "FR":
            case "All Others":
                return temperature * 1.8 - 459.67;
            default:
                return temperature;
        }
    }

    private Double getCelsiusTemperature(String observatory, Double temperature) {
        switch (observatory) {
            case "US":
                return ((temperature - 32) * 5) / 9;
            case "FR":
            case "All Others":
                return temperature - 273.15F;
            default:
                return temperature;
        }
    }

    private Double convertDistanceFromLastPoint(Double lastKnownLocation, Distance scale) {
        switch (scale) {
            case MILES:
                double conversionFactor = 1.609344;
                return lastKnownLocation / conversionFactor;
            case METERS:
                return lastKnownLocation * 1000;
            default:
                return 0.0;
        }
    }

    private List<String> getFileData() throws IOException {
        File dataFile = new File("classpath:flyingData.txt");
         return Files.lines(dataFile.toPath()).collect(Collectors.toList());
    }

}
