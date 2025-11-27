import java.net.HttpURLConnection
import java.net.URL

plugins {
    java
    application
}

group = "com.aoc"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("fetchInput") {
    description = "Fetches puzzle input for a specific day"
    group = "adventOfCode"

    doLast {
        val day = project.findProperty("day")?.toString() ?: "1"
        val year = project.findProperty("year")?.toString() ?: "2025"
        val sessionToken = System.getenv("AOC_SESSION_TOKEN")
            ?: throw GradleException("AOC_SESSION_TOKEN environment variable is not set")

        val dayNum = day.toInt()
        val dayDir = file("src/main/resources/day%02d".format(dayNum))
        val inputFile = File(dayDir, "input.txt")

        if (inputFile.exists()) {
            println("Input already exists for day $dayNum, skipping...")
            return@doLast
        }

        dayDir.mkdirs()

        val urlString = "https://adventofcode.com/$year/day/$dayNum/input"
        println("Fetching input for day $dayNum from $urlString...")

        @Suppress("DEPRECATION")
        val connection = URL(urlString).openConnection() as HttpURLConnection
        connection.setRequestProperty("Cookie", "session=$sessionToken")

        if (connection.responseCode == 200) {
            inputFile.writeText(connection.inputStream.bufferedReader().readText())
            println("Successfully downloaded input for day $dayNum")
        } else {
            throw GradleException("Failed to fetch input: HTTP ${connection.responseCode}")
        }
    }
}

tasks.register("fetchAllInputs") {
    description = "Fetches puzzle inputs for all 12 days"
    group = "adventOfCode"

    doLast {
        val year = project.findProperty("year")?.toString() ?: "2025"
        println("Fetching inputs for all days of $year...")

        (1..12).forEach { day ->
            try {
                val fetchInputTask = tasks.named("fetchInput").get()
                project.setProperty("day", day.toString())
                fetchInputTask.actions.forEach { action ->
                    action.execute(fetchInputTask)
                }
            } catch (e: Exception) {
                println("Warning: Could not fetch day $day: ${e.message}")
            }
        }
    }
}

tasks.register<JavaExec>("runDay") {
    description = "Runs the solution for a specific day"
    group = "adventOfCode"

    val day = project.findProperty("day")?.toString() ?: "1"
    val dayNum = "%02d".format(day.toInt())

    mainClass.set("aoc.day$dayNum.Day$dayNum")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register("runAll") {
    description = "Runs all day solutions"
    group = "adventOfCode"

    doLast {
        (1..12).forEach { day ->
            val dayNum = "%02d".format(day)

            try {
                println("\n=== Day $day ===")
                tasks.register<JavaExec>("runDay$dayNum") {
                    mainClass.set("aoc.day$dayNum.Day$dayNum")
                    classpath = sourceSets["main"].runtimeClasspath
                }.get().exec()
            } catch (e: Exception) {
                println("Day $day not implemented yet or failed: ${e.message}")
            }
        }
    }
}
