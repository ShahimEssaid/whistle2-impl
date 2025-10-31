plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.34.0"
}


dependencies {
    implementation(libs.google.whistle.runtime)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

publishing {
    repositories {
        maven {
            name = "GitHubSEPackages"
            url = uri("https://maven.pkg.github.com/ShahimEssaid/m2")
            credentials(PasswordCredentials::class)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    pom {
        developers {
            developer {
                id = "ShahimEssaid"
                name = "Shahim Essaid"
                email = "shahim@essaid.com"
            }
        }

        scm {
            url =
                "https://github.com/ShahimEssaid/examples-2024/tree/main/gradle-builds/java-17-lib-build-kts"
        }
    }
}


repositories {
    mavenCentral()
    maven {
        name = "CentralSnapshots"
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
    }
    maven {
        name = "GitHubSEPackages"
        url = uri("https://maven.pkg.github.com/ShahimEssaid/m2")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(property("java_version") as String)
    }
    withSourcesJar()
}

tasks.withType<Jar> { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }

tasks.named<Test>("test") {
    useJUnitPlatform()
}
