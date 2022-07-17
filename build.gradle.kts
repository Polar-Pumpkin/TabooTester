plugins {
    java
    id("io.izzel.taboolib") version "1.40"
    id("org.jetbrains.kotlin.jvm") version "1.6.20"
}

taboolib {
    description {
        contributors {
            name("EntityParrot_")
        }
        dependencies {
            name("Adyeshach").optional(true)
        }
    }

    install("common")
    install("common-5")

    install("module-nms")
    install("module-nms-util")
    install("module-lang")
    install("module-chat")
    install("module-configuration")

    install("platform-bukkit")

    classifier = null
    version = "6.0.9-31"
}

repositories {
    mavenCentral()
}

val exposedVersion: String by project
dependencies {
    compileOnly("ink.ptms.core:v11800:11800:api")
    compileOnly("ink.ptms.core:v11800:11800:mapped")
    compileOnly("ink.ptms.core:v11800:11800:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))

    compileOnly("com.h2database:h2:2.1.214")
    compileOnly("com.google.guava:guava:31.0.1-jre")
    compileOnly("org.jetbrains.exposed:exposed-core:$exposedVersion")
    compileOnly("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    compileOnly("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    compileOnly("ink.ptms:Adyeshach:1.5.7")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}