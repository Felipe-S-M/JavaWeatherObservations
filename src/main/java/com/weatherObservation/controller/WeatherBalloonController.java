package com.weatherObservation.controller;

import com.weatherObservation.dto.request.GenerateFilteredFileRequest;
import com.weatherObservation.dto.response.GenerateFlightStatisticsResponse;
import com.weatherObservation.service.GenerateDataFileService;
import com.weatherObservation.service.GenerateFilteredFileService;
import com.weatherObservation.service.GenerateFlightStatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.text.ParseException;

@RequestMapping("/weatherBalloon")
@AllArgsConstructor
@Controller
public class WeatherBalloonController {
    private GenerateDataFileService generateDataFileService;
    private GenerateFilteredFileService generateFilteredFileService;
    private GenerateFlightStatisticsService generateFlightStatisticsService;

    @PostMapping("/createReportFile")
    public ResponseEntity<?> generateFile() throws IOException, ParseException {
        generateDataFileService.generateFile();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/convertData")
    public ResponseEntity<?> convertData(
            @RequestBody GenerateFilteredFileRequest request) throws Exception {
        generateFilteredFileService.generateFilteredFile(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/flightStatistics")
    public ResponseEntity<GenerateFlightStatisticsResponse> flightStatistics() throws Exception {
        return ResponseEntity.ok(generateFlightStatisticsService.generateFlightStatistics());
    }

}
