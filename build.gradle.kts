import java.net.HttpURLConnection
import java.net.URL

plugins {
    java
    application
    checkstyle
    pmd
    id("com.github.spotbugs") version "6.4.7"
    id("com.github.ben-manes.versions") version "0.53.0"
    id("se.patrikerdes.use-latest-versions") version "0.2.19"
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
    implementation("org.slf4j:slf4j-api:2.1.0-alpha1")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.21")

    testImplementation(platform("org.junit:junit-bom:6.1.0-M1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly("org.jetbrains:annotations:26.0.2-1")
}

tasks.test {
    useJUnitPlatform()
}

checkstyle {
    toolVersion = "10.20.1"
    isIgnoreFailures = false
    maxWarnings = 0
}

pmd {
    toolVersion = "7.19.0"
    isIgnoreFailures = false
    ruleSets = listOf()
    ruleSetFiles = files("config/pmd/ruleset.xml")
    threads = 4
}

spotbugs {
    toolVersion = "4.9.8"
    ignoreFailures = false
    showProgress = true
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask>().configureEach {
    reports {
        maybeCreate("html").required.set(true)
        maybeCreate("xml").required.set(false)
    }
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
