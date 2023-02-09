package com.weatherObservation.service;

import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Service
public class GenerateDataFileService {

    private FileUtilsService fileUtilsService;

    public void generateFile() throws IOException, ParseException {
        fileUtilsService.deleteExitingFile("flyingData");
        var file = new File(fileUtilsService.getDefaultFolderPath() + "/flyingData.txt");
        file.createNewFile();
        generatedData(file);
    }

    private void generatedData(File file) throws IOException, ParseException {
        StringBuilder content = new StringBuilder();
        StringBuilder temperature = new StringBuilder();
        StringBuilder location = new StringBuilder();
        buildHeader(content);
        FileWriter fw = new FileWriter(file);

        for (int i = 0; i < 1_000_000; i++) {
            content.append(buildObservationContent(i, temperature, location));
            fw.write(content.toString());
            content.setLength(0);
            temperature.setLength(0);
            location.setLength(0);
        }
        fw.close();
    }

    private String buildObservationContent(
            Integer numLine, StringBuilder temperature, StringBuilder location) throws ParseException {
        var randomObservatory = generateRandomObservatory();
        switch (randomObservatory) {
            case "AU":
                generateObservatoryData(
                        temperature,
                        -88,
                        58,
                        location,
                        -38.168786,
                        145.366598,
                        false
                );
                break;
            case "US":
                generateObservatoryData(
                        temperature,
                        -460,
                        212,
                        location,
                        26.007706,
                        -80.388898,
                        false
                );
                break;
            case "FR":
                generateObservatoryData(
                        temperature,
                        0,
                        373,
                        location,
                        45.599727,
                        2.609266,
                        false
                );
                break;
            case "All Others":
                generateObservatoryData(
                        temperature,
                        0,
                        373,
                        location,
                        0.0,
                        0.0,
                        true
                );
                break;
        }
        return numLine + "|" + buildTimeStamp() + "|" + location + "|" + temperature + "|" + randomObservatory + "\n";
    }

    private String buildTimeStamp() throws ParseException {
        Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse("30/12/1999");
        Date d2 = new SimpleDateFormat("dd/MM/yyyy").parse("30/12/2020");
        Date randomDate = new Date(ThreadLocalRandom.current()
                .nextLong(d1.getTime(), d2.getTime()));
        SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        return isoFormatter.format(randomDate);
    }

    private String generateRandomObservatory() {
        int observatoryChance = buildRandom().nextInt(100);
        if (observatoryChance <= 25)
            return "AU";
        if (observatoryChance<=50)
            return "US";
        if (observatoryChance<=75)
            return "FR";
        return "All Others";
    }

    private void buildHeader(StringBuilder content) {
        content.append("id|timestamp|location|temperature|observatory\n");
    }

    private void generateObservatoryData(
            StringBuilder temperature,
            Integer minTemperature,
            Integer maxTemperature,
            StringBuilder location,
            Double pointX,
            Double pointY,
            Boolean generateRandomLocation
    ) {
        temperature.append(buildRandom().nextInt(maxTemperature - minTemperature) + minTemperature);
        location.append(generateRandomLocation ? generateRandomLocation() : pointX + "," + pointY);
    }

    private String generateRandomLocation() {
        Double pointX = ((-100) + (100 - (-100)) * buildRandom().nextDouble());
        Double pointY = ((-100) + (100 - (-100)) * buildRandom().nextDouble());
        return pointX + "," + pointY;
    }

    private Random buildRandom() {
        return new Random();
    }

}
