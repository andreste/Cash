// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val kotlinVersion = "1.9.0"
    kotlin("jvm") version kotlinVersion apply false
    kotlin("kapt") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
    kotlin("android") version kotlinVersion apply false

    id("com.android.application") version "8.3.1" apply false
    id("com.android.library") version "8.3.1" apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
}
buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
        maven("https://jitpack.io")
        google()
        mavenCentral()
    }
}