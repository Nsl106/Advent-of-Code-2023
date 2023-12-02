plugins {
    kotlin("jvm") version "1.9.20"
    application
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}