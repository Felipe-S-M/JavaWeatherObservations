package com.weatherObservation.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ObservationsResponse {
    private String au;
    private String us;
    private String fr;
    private String AllOthers;
}
