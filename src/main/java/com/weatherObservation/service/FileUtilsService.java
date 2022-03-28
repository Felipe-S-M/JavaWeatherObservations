package com.weatherObservation.service;

import com.weatherObservation.WeatherObservationApplication;
import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

@AllArgsConstructor
@Service
public class FileUtilsService {

    private ResourceLoader resourceLoader;

    public String getDefaultFolderPath() throws IOException {
        return new ClassPathResource(".").getFile().getPath();
    }

    public boolean checkIfExistFile(String fileName){
        try {
            ResourceUtils.getFile(getDefaultFolderPath() + fileName);
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    public void deleteExitingFile(String fileName) throws FileNotFoundException {
        File file;
        if (checkIfExistFile(fileName)) {
            file = ResourceUtils.getFile(fileName);
            file.delete();
        }
    }
}
