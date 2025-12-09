import java.io.ByteArrayOutputStream
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

abstract class UpdateReadmeTask : DefaultTask() {
    @get:InputFiles
    abstract val runtimeClasspath: ConfigurableFileCollection

    @get:OutputFile
    abstract val readmeFile: RegularFileProperty

    init {
        // Always run this task - timing results vary between runs
        outputs.upToDateWhen { false }
    }

    @TaskAction
    fun execute() {
        val results = mutableListOf<Map<String, String>>()
        val cp = runtimeClasspath.asPath

        (1..12).forEach { day ->
            val dayNum = "%02d".format(day)
            val dayMainClass = "aoc.day$dayNum.Day$dayNum"

            try {
                val outputStream = ByteArrayOutputStream()
                val process = ProcessBuilder("java", "-cp", cp, dayMainClass)
                    .redirectErrorStream(true)
                    .start()

                process.inputStream.copyTo(outputStream)
                val exitCode = process.waitFor()

                if (exitCode == 0) {
                    val outputText = outputStream.toString()
                    val part1 = Regex("""Part 1: (\d+)""").find(outputText)?.groupValues?.get(1) ?: "-"
                    val part2 = Regex("""Part 2: (\d+)""").find(outputText)?.groupValues?.get(1) ?: "-"
                    val time = Regex("""Completed in (.+)""").find(outputText)?.groupValues?.get(1) ?: "-"

                    results.add(mapOf("day" to day.toString(), "part1" to part1, "part2" to part2, "time" to time))
                    println("Day $day: Part1=$part1, Part2=$part2, Time=$time")
                } else {
                    println("Day $day: Not implemented")
                }
            } catch (e: Exception) {
                println("Day $day: Not implemented")
            }
        }

        // Get machine specs
        val machineSpecs = getMachineSpecs()

        // Update README
        val readme = readmeFile.get().asFile
        val content = readme.readText()

        val tableHeader = "## Performance\n\n$machineSpecs\n\n| Day | Time |\n|-----|------|"

        val tableRows = results.joinToString("\n") { result ->
            "| ${result["day"]} | ${result["time"]} |"
        }

        val performanceSection = "$tableHeader\n$tableRows\n"

        val newContent = if (content.contains("## Performance")) {
            content.replace(
                Regex("""## Performance\n\n(\*\*Machine:\*\*.*?\n\n)?\|.*?\n\|[-|\s]+\n(\|.*\n)*"""),
                performanceSection
            )
        } else {
            content.trimEnd() + "\n\n" + performanceSection
        }

        readme.writeText(newContent)
        println("\nREADME.md updated with performance table")
    }

    private fun getMachineSpecs(): String {
        return try {
            val process = ProcessBuilder("system_profiler", "SPHardwareDataType")
                .redirectErrorStream(true)
                .start()
            val output = process.inputStream.bufferedReader().readText()
            process.waitFor()

            val chip = Regex("""Chip: (.+)""").find(output)?.groupValues?.get(1)?.trim() ?: "Unknown"
            val memory = Regex("""Memory: (.+)""").find(output)?.groupValues?.get(1)?.trim() ?: "Unknown"
            val cores = Regex("""Total Number of Cores: (.+)""").find(output)?.groupValues?.get(1)?.trim() ?: "Unknown"

            "**Machine:** $chip, $cores cores, $memory RAM"
        } catch (e: Exception) {
            "**Machine:** Unknown"
        }
    }
}

tasks.register<UpdateReadmeTask>("updateReadme") {
    description = "Runs all solutions and updates README with performance table"
    group = "adventOfCode"
    dependsOn("classes")

    runtimeClasspath.from(sourceSets["main"].runtimeClasspath)
    readmeFile.set(layout.projectDirectory.file("README.md"))
}
