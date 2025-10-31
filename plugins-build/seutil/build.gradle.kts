plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.34.0"
}

//publishing {
//    publications {
//        create<MavenPublication>("mavenJava") {
//            from(components["java"])
//
//            pom {
//                name.set("Lib")
//                description.set("Lib description.")
//                developers {
//                    developer {
//                        name.set("Shahim Essaid")
//                        email.set("shahim@essaid.com")
//                    }
//                }
//            }
//        }
//    }
//}

//repositories {
//    mavenCentral()
//    maven {
//        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
//    }
//}


dependencies {
    implementation(libs.google.whistle.runtime)
    implementation(libs.essaid.whistle.common)
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



tasks.withType<Jar> { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }

