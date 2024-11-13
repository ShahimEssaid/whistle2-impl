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
    implementation("com.essaid.whistle.utils:cli")
    implementation("com.essaid.whistle.utils:spark")

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
//    mainClass = "com.google.cloud.verticals.foundations.dataharmonization.Main"
    mainClass = "com.essaid.whistle.cli.Main"
    applicationName = "se-whistle2-cli"
}


tasks.withType<Tar>().configureEach{
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Zip>().configureEach{
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Sync.html

//Execution failed for task ':installDist'.
//> Entry lib/common-0.0.1-SNAPSHOT.jar is a duplicate but no duplicate handling strategy has been set. Please refer to https://docs.gradle.org/8.7/dsl/org.gradle.api.tasks.Copy.html#org.gradle.api.tasks.Copy:duplicatesStrategy for details.
tasks.withType<Sync>().configureEach{
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


//getTasksByName("installDist", false).forEach(){ _ ->
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//}


//tasks.withType<Tar>{
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//}

// https://stackoverflow.com/questions/69134136/gradle-7-task-disttar-is-a-duplicate-but-no-duplicate-handling-strategy-has-b#answer-75606109
//val distTar by tasks.getting(Tar::class){
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//}

//tasks.withType(Zip){
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//}


//tasks.named<Copy>("distTar"){
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//}