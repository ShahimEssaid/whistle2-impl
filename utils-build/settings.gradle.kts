plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "utils-build"
include("common")
include( "cli")
include("spark")

