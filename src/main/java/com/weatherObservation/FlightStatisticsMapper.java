package com.weatherObservation;

import com.google.common.util.concurrent.AtomicDouble;
import com.weatherObservation.dto.response.GenerateFlightStatisticsResponse;
import com.weatherObservation.dto.response.ObservationsResponse;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

public class FlightStatisticsMapper {
    public static GenerateFlightStatisticsResponse buildFlightStatistics(
            Double minTemperature,
            Double maxTemperature,
            Double meanTemperature,
            AtomicDouble totalDistance,
            Map<String, Integer> observationsEachObservatory
    ) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return GenerateFlightStatisticsResponse.builder()
                .minimumTemperature(formatter.format(minTemperature))
                .maximumTemperature(formatter.format(maxTemperature))
                .meanTemperature(formatter.format(meanTemperature))
                .observations(buildObservationsResponse(observationsEachObservatory))
                .totalDistanceTravelled(formatter.format(totalDistance))
                .build();
    }

    public static ObservationsResponse buildObservationsResponse(Map<String, Integer> observationsEachObservatory) {
        return ObservationsResponse.builder()
                .au(observationsEachObservatory.get("AU").toString())
                .fr(observationsEachObservatory.get("FR").toString())
                .us(observationsEachObservatory.get("US").toString())
                .AllOthers(observationsEachObservatory.get("US").toString())
                .build();
    }
}
