import net.ltgt.gradle.errorprone.errorprone

plugins {
    application
    checkstyle
    jacoco
    id("com.diffplug.spotless") version "8.10.4"
    id("net.ltgt.errorprone") version "5.2.0"
}

group = "edu.hanover.sdp2026"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.googlecode.lanterna:lanterna:3.1.5")
    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.23.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.23.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    errorprone("com.google.errorprone:error_prone_core:2.51.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "edu.hanover.sdp2026.Main"
}

tasks.withType<JavaCompile>().configureEach {
    // Serial warnings are noise for RuntimeException subclasses without a serialVersionUID.
    options.compilerArgs.add("-Xlint:all,-serial")
    options.errorprone.disableWarningsInGeneratedCode.set(true)
}

tasks.test {
    useJUnitPlatform()
}

checkstyle {
    configFile = file("config/checkstyle/checkstyle.xml")
    maxWarnings = 0
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required = true
        html.required = true
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.required = true
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}

spotless {
    java {
        target("src/**/*.java")
        palantirJavaFormat("2.103.0")
    }
    format("misc") {
        target("*.md", ".gitignore", "*.gradle.kts", ".github/**/*.yml")
        trimTrailingWhitespace()
        endWithNewline()
    }
}
