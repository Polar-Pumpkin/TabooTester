plugins {
    java
    id("io.izzel.taboolib") version "1.34"
    id("org.jetbrains.kotlin.jvm") version "1.5.30"
}

taboolib {
    description {
        contributors {
            name("EntityParrot_")
        }
        dependencies {

        }
    }

    install("common")

    install("module-lang")
    install("module-chat")
    install("module-configuration")

    install("platform-bukkit")

    classifier = null
    version = "6.0.7-20"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v11800:11800:api")
    compileOnly("ink.ptms.core:v11800:11800:mapped")
    compileOnly("ink.ptms.core:v11800:11800:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))

    compileOnly("com.google.guava:guava:31.0.1-jre")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}