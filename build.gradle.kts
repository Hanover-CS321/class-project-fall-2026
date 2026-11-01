plugins {
    application
    id("com.diffplug.spotless") version "8.10.4"
}

group = "edu.hanover.sdp2026"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.23.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.23.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "edu.hanover.sdp2026.Main"
}

tasks.test {
    useJUnitPlatform()
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
