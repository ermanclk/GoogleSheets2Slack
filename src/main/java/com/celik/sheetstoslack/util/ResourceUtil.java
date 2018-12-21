package com.celik.sheetstoslack.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class ResourceUtil {

    private static final String SHEET_CONFIG_LOCATION = "\\sheet-templates\\";
    private static final String DELIMITER = "=";
    private static final String WORKING_DIR = "user.dir";
    private static final String CONFIG_FILE_SUFFIX = ".sheet";

    public Map<String, String> readSheetConfiguration(Path resource) throws IOException {
        String delimiter = DELIMITER;
        Map<String, String> map = new HashMap<>();
        try (Stream<String> lines = Files.lines(resource)) {
            lines.filter(line -> line.contains(delimiter)).forEach(
                    line -> map.putIfAbsent(line.split(delimiter)[0], line.split(delimiter)[1])
            );
        }
        return map;
    }

    public List<Path> loadSheetResources() throws IOException {
        return loadSheetResources(SHEET_CONFIG_LOCATION);
    }

    private List<Path> loadSheetResources(String location) throws IOException {
        File file = new File( System.getProperty(WORKING_DIR) + location);
        List<Path> paths = new ArrayList<>();
        if (file.listFiles() == null) {
            throw new IllegalStateException("config folder should be present in working directory..");
        }
        for (final File fileEntry : file.listFiles()) {
            if (fileEntry.getName().endsWith(CONFIG_FILE_SUFFIX)) {
                paths.add(fileEntry.toPath());
            }
        }
        return paths;
    }
}