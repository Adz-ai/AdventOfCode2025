package aoc.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FileUtils {

  private static final String TXT_EXTENSION = ".txt";
  private static final String RESOURCES_PATH = "src/main/resources";

  @Contract("_ -> new")
  public static @NotNull List<String> readLines(String filename) throws IOException {
    return Files.readAllLines(resolveInputPath(filename));
  }

  public static @NotNull String readAsString(String filename) throws IOException {
    return Files.readString(resolveInputPath(filename));
  }

  private static @NotNull Path resolveInputPath(@NotNull String filename) {
    String fullFilename =
        filename.endsWith(TXT_EXTENSION) ? filename : filename + TXT_EXTENSION;
    return Path.of(RESOURCES_PATH, fullFilename);
  }

  public static @NotNull List<List<String>> splitByBlankLines(@NotNull List<String> lines) {
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
