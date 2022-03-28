package com.weatherObservation.service;

import com.weatherObservation.dto.request.GenerateFilteredFileRequest;
import com.weatherObservation.entity.Distance;
import com.weatherObservation.entity.Temperature;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GenerateFilteredFileService {

    private FileUtilsService fileUtilsService;

    public void generateFilteredFile(GenerateFilteredFileRequest request) throws Exception{
        if(!fileUtilsService.checkIfExistFile("flyingData.txt")){
            throw new Exception("Data file not found!");
        }
        fileUtilsService.deleteExitingFile("flyingDataFiltered.txt");
        File file = new File("flyingDataFiltered.txt");
        file.createNewFile();
        buildFile(file, request);

    }

    private void buildFileHeader(StringBuilder content) {
        content.append("id|timestamp|location|temperature|observatory|distanceFromLastObservation\n");
    }

    private void buildFile(File file, GenerateFilteredFileRequest request) throws IOException {
        StringBuilder content = new StringBuilder();
        StringBuilder lastKnownPoint = new StringBuilder();
        buildFileHeader(content);
        FileWriter fw = new FileWriter(file);
        fw.write(content.toString());

        for (String line : getFileData()) {
            clearStringBuilder(content);
            String[] fileContent = line.split("\\|");
            buildConvertedFileContent(fileContent, request, lastKnownPoint, content, fw);
        }
        fw.close();
    }

    private void clearStringBuilder(StringBuilder content) {
        content.setLength(0);
    }

    private void buildConvertedFileContent(
            String[] fileContent,
            GenerateFilteredFileRequest request,
            StringBuilder lastKnownPoint,
            StringBuilder content,
            FileWriter fileWriter
    ) throws IOException {
        if (!fileContent[0].equals("id")) {
            Double distanciaFromLastPoin = calculateDistanceFromLastPoint(lastKnownPoint, fileContent[2]);
            clearStringBuilder(lastKnownPoint);
            lastKnownPoint.append(fileContent[2]);
            content.append(buildContentLine(fileContent, request, distanciaFromLastPoin));
            fileWriter.write(content + "\n");
        }
    }

    private String buildContentLine(
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
        return line.toString();
    }
 
    private String formatNumber(Double number) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(number);
    }

    private Double calculateDistanceFromLastPoint(StringBuilder lastKnownLocation, String currentLocation) {
        if (lastKnownLocation.toString().isEmpty())
            return 0.0;
        String[] lastPoints = lastKnownLocation.toString().split("\\,");
        Double x1 = Double.valueOf(lastPoints[0]);
        Double x2 = Double.valueOf(lastPoints[1]);

        String[] currentPoints = currentLocation.split(",");
        Double y1 = Double.valueOf(currentPoints[0]);
        Double y2 = Double.valueOf(currentPoints[1]);

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
        File dataFile = new File(fileUtilsService.getDefaultFolderPath() + "/flyingData.txt");
        return Files.lines(dataFile.toPath()).collect(Collectors.toList());
    }

}
