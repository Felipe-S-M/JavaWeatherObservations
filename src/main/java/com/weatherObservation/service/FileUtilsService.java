package com.weatherObservation.service;

import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FileUtilsService {
    public static boolean checkIfExistFile(String fileName) throws FileNotFoundException {
        var file = ResourceUtils.getFile("classpath:" + fileName);
        return file != null;
    }

    public static void deleteExitingFile(String fileName) throws FileNotFoundException {
        File file;
        if (FileUtilsService.checkIfExistFile(fileName)) {
            file = ResourceUtils.getFile("classpath:" + fileName);
            file.delete();
        }
    }

    public static void writeInFile(File file, String content) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.write(content);
        fw.close();
    }
}
