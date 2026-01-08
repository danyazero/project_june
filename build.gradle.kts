plugins {
    java
    antlr
    kotlin("jvm") version "2.2.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.9")
    antlr("org.antlr:antlr4:4.13.2")
}

sourceSets.main {
    antlr.srcDirs("src/main/antlr")
}

tasks {
    compileKotlin {
        dependsOn(generateGrammarSource)
    }
    generateGrammarSource {
        maxHeapSize = "64m"
        arguments = arguments + listOf("-visitor", "-no-listener", "-package", "com.danyazero")
        outputDirectory = File("build/generated-src/antlr/main/com/danyazero")
    }
}


kotlin {
    jvmToolchain(24)
}
