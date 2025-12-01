package aoc.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    private static final String TXT_EXTENSION = ".txt";
    private static final String RESOURCES_PATH = "src/main/resources";

    public static List<String> readLines(String filename) throws IOException {
        return Files.readAllLines(resolveInputPath(filename));
    }

    public static String readAsString(String filename) throws IOException {
        return Files.readString(resolveInputPath(filename));
    }

    private static Path resolveInputPath(String filename) {
        String fullFilename = filename.endsWith(TXT_EXTENSION) ? filename : filename + TXT_EXTENSION;
        return Path.of(RESOURCES_PATH, fullFilename);
    }

    public static List<Long> toLongList(List<String> input) {
        return input.stream()
                .map(Long::parseLong)
                .toList();
    }

    public static List<List<String>> splitByBlankLines(List<String> lines) {
        var groups = new ArrayList<List<String>>();
        var currentGroup = new ArrayList<String>();

        for (var line : lines) {
            if (line.isBlank()) {
                if (!currentGroup.isEmpty()) {
                    groups.add(new ArrayList<>(currentGroup));
                    currentGroup.clear();
                }
            } else {
                currentGroup.add(line);
            }
        }

        // Add last group if not empty
        if (!currentGroup.isEmpty()) {
            groups.add(currentGroup);
        }

        return groups;
    }
}
