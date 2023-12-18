plugins {
    kotlin("jvm") version "1.9.20"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}