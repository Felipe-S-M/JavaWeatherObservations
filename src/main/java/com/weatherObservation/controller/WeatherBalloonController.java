package com.weatherObservation.controller;

import com.weatherObservation.dto.request.GenerateFilteredFileRequest;
import com.weatherObservation.dto.response.GenerateFlightStatisticsResponse;
import com.weatherObservation.service.GenerateDataFileService;
import com.weatherObservation.service.GenerateFilteredFileService;
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
    private com.weatherObservation.service.generateFlightStatisticsService generateFlightStatisticsService;

    @PostMapping("/generateFile")
    public ResponseEntity<?> generateFile() throws IOException, ParseException {
        generateDataFileService.generateFile();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/generateOutputFilteredFile")
    public ResponseEntity<?> generateOutputFilteredFile(
            @RequestBody GenerateFilteredFileRequest request) throws Exception {
        generateFilteredFileService.generateFilteredFile(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/generateFlightStatistics")
    public GenerateFlightStatisticsResponse generateFlightStatistics() throws Exception {
        return generateFlightStatisticsService.generateFlightStatistics();
    }

}
