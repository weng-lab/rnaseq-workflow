import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.3.10"
    id("application")
    id("com.github.johnrengelman.shadow") version "4.0.2"
}

version = "1.0"

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("io.krews", "krews", "0.8.1")
}

application {
    mainClassName = "RnaSeqWorkflowKt"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    baseName = "rnaseq"
    classifier = ""
    destinationDir = file("build")
}