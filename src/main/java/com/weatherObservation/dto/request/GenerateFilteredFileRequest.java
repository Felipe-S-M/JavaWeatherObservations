package com.weatherObservation.dto.request;

import com.weatherObservation.entity.Distance;
import com.weatherObservation.entity.Temperature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenerateFilteredFileRequest {
    private Distance distanceScale;
    private Temperature temperatureScale;
}
