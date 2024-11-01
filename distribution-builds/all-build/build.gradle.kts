plugins {
    application
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

dependencies {
    implementation("com.essaid.groupId.com.google.cloud.verticals.foundations.dataharmonization:runtime:dev-SNAPSHOT")
    implementation("com.essaid.groupId.com.google.cloud.verticals.foundations.dataharmonization.plugins.harmonization:hdev2:dev-SNAPSHOT")
    implementation("com.essaid.groupId.com.google.cloud.verticals.foundations.dataharmonization.plugins:logging:dev-SNAPSHOT")
    implementation("com.essaid.groupId.com.google.cloud.verticals.foundations.dataharmonization.plugins:test:dev-SNAPSHOT")

    implementation("com.essaid.whistle.plugin:seutil")

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    withSourcesJar()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass = "com.google.cloud.verticals.foundations.dataharmonization.Main"
    applicationName = "se-whistle2-cli"
}