package com.weatherObservation.service;

import lombok.var;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GenerateDataFileService {

    public void generateFile() throws IOException, ParseException {
        FileUtilsService.deleteExitingFile("flyingData");
        var file = new File("classpath:/flyingData.txt");
        file.createNewFile();
        generatedData(file);
    }

    private void generatedData(File file) throws IOException, ParseException {
        StringBuilder content = new StringBuilder();
        buildHeader(content);
        FileUtilsService.writeInFile(file, content.toString());

        for (int i = 0; i < 50_000; i++) {
            StringBuilder temperature = new StringBuilder();
            StringBuilder location = new StringBuilder();

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
            }
            content.append(buildObservationContent(location.toString(), temperature.toString(), randomObservatory, i));
            FileUtilsService.writeInFile(file, content.toString());
            content.setLength(0);
        }
    }

    private String buildObservationContent(
            String location, String temperature, String observatroy, Integer numberLine) throws ParseException {
        return numberLine + "|" + buildTimeStamp() + "|" + location + "|" + temperature + "|" + observatroy + "\n";
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
        location.append(generateRandomLocation ? generateRandomLocation() : pointX+","+pointY);
    }

    private String generateRandomLocation() {
        return ((-100) + (100 - (-100)) * buildRandom().nextDouble()) + "," +
                ((-100) + (100 - (-100)) * buildRandom().nextDouble());
    }

    private Random buildRandom() {
        return new Random();
    }

}
