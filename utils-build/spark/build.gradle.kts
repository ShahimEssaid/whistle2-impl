plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.34.0"
    idea
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
    implementation(libs.spark.sql)
    implementation(project(":common"))
    implementation(libs.picocli.picocli)
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
    jvmArgs = listOf(
        "-XX:+IgnoreUnrecognizedVMOptions",
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED",
        "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
        "--add-opens=java.base/java.io=ALL-UNNAMED",
        "--add-opens=java.base/java.net=ALL-UNNAMED",
        "--add-opens=java.base/java.nio=ALL-UNNAMED",
        "--add-opens=java.base/java.util=ALL-UNNAMED",
        "--add-opens=java.base/java.util.concurrent=ALL-UNNAMED",
        "--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED",
        "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
        "--add-opens=java.base/sun.nio.cs=ALL-UNNAMED",
        "--add-opens=java.base/sun.security.action=ALL-UNNAMED",
        "--add-opens=java.base/sun.util.calendar=ALL-UNNAMED",
        "--add-opens=java.security.jgss/sun.security.krb5=ALL-UNNAMED"
    )
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
