package com.weatherObservation.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GenerateFlightStatisticsResponse {
    private String minimumTemperature;
    private String maximumTemperature;
    private String meanTemperature;
    private ObservationsResponse observations;
    private String totalDistanceTravelled;
}
