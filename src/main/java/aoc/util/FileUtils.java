package aoc.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<String> readLines(String filename) throws IOException {
        var fullFilename = filename.endsWith(".txt") ? filename : filename + ".txt";
        var path = Path.of("src/main/resources", fullFilename);
        return Files.readAllLines(path);
    }

    public static String readAsString(String filename) throws IOException {
        var fullFilename = filename.endsWith(".txt") ? filename : filename + ".txt";
        var path = Path.of("src/main/resources", fullFilename);
        return Files.readString(path);
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
