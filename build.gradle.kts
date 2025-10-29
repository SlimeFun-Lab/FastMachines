import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.1.10"
    id("com.gradleup.shadow") version "8.3.5"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.alessiodp.com/releases/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(kotlin("stdlib")) // loaded through library loader
    compileOnly(kotlin("reflect")) // loaded through library loader
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    compileOnly("com.github.SlimeFun-Lab:Slimefun4:6d5694e")
    compileOnly("com.github.SlimeFun-Lab:SlimefunTranslation:8007216d6c")
    compileOnly("com.github.SlimeFun-Lab:SlimeHUD:97d3a09130")
    compileOnly("com.github.SlimeFun-Lab:InfinityExpansion:78b140c13f")
    compileOnly("com.github.SlimeFun-Lab:Networks:aa3acab913")
    compileOnly("com.github.VoperAD:SlimeFrame:8af2379a01")
    implementation("org.bstats:bstats-bukkit:3.1.0")
    implementation("com.github.SlimeFun-Lab:guizhanlib:94c4abd25b")
    implementation("com.github.SlimeFun-Lab:guizhanlib-kt:f5c4375dea")

    testImplementation(kotlin("test"))
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.10.0")
}

group = "net.guizhanss"
description = "FastMachines"

val mainPackage = "net.guizhanss.fastmachines"

java {
    disableAutoTargetJvm()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    compilerOptions {
        jvmToolchain(21)
        compilerOptions {
            javaParameters = true
            jvmTarget = JvmTarget.JVM_21
        }
    }
}

tasks.shadowJar {
    fun doRelocate(from: String, to: String? = null) {
        val last = to ?: from.split(".").last()
        relocate(from, "$mainPackage.libs.$last")
    }

    doRelocate("net.byteflux.libby")
    doRelocate("net.guizhanss.guizhanlib")
    doRelocate("org.bstats")
    doRelocate("io.papermc.lib", "paperlib")
    minimize()
    archiveClassifier = ""
}

bukkit {
    main = "$mainPackage.FastMachines"
    apiVersion = "1.18"
    authors = listOf("ybw0014")
    description = "More Slimefun machines that bulk craft items with all shapeless recipes"
    depend = listOf("Slimefun")
    softDepend = listOf(
        "GuizhanLibPlugin",
        "SlimefunTranslation",
        "InfinityExpansion",
        "SlimeFrame",
        "SlimeHUD",
        "Networks",
    )
    loadBefore = listOf(
        "SlimeCustomizer",
        "RykenSlimeCustomizer",
        "SlimeFunRecipe"
    )
}

tasks.runServer {
    downloadPlugins {
        // Slimefun
        url("https://blob.build/dl/Slimefun4/Dev/latest")
        // SlimeHUD
        url("https://blob.build/dl/SlimeHUD/Dev/latest")
        // GuizhanCraft for testing convenient
        url("https://builds.guizhanss.com/api/download/ybw0014/GuizhanCraft/master/latest")
    }
    jvmArgs("-Dcom.mojang.eula.agree=true")
    minecraftVersion("1.20.6")
}

tasks.test {
    useJUnitPlatform()
}
