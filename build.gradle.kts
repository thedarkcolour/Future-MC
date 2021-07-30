import net.minecraftforge.gradle.common.util.RunConfig
import wtf.gofancy.fancygradle.patch.Patch
import wtf.gofancy.fancygradle.script.extensions.curse
import java.time.LocalDateTime

plugins {
    kotlin("jvm") version "1.3.50"
    java
    idea
    id("net.minecraftforge.gradle") version "5.0.+"
    id("wtf.gofancy.fancygradle") version "1.0.1"
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))
idea.module.inheritOutputDirs = true

version = "1.0.0"
group = "thedarkcolour.futuremc"

minecraft (Action {
    mappings("stable", "39-1.12")

    accessTransformer("src/main/resources/META-INF/futuremc_at.cfg")

    runs(Action {
        val config = Action<RunConfig> {
            properties(mapOf(
                "forge.logging.markers" to "COREMODLOG",
                "forge.logging.console.level" to "debug",
                "fml.coreMods.load" to "thedarkcolour.futuremc.asm.CoreLoader"
            ))
            workingDirectory = project.file("run" + if (name == "server") "/server" else "").canonicalPath
            source(sourceSets["main"])
        }

        create("client", config)
        create("server", config)
    })
})

fancyGradle (Action {
    patches ( Action {
        patch(Patch.RESOURCES, Patch.COREMODS, Patch.CODE_CHICKEN_LIB, Patch.ASM)
    })
})

repositories {
    jcenter()

    maven { url = uri("https://jitpack.io") }
    maven {
        name = "CraftTweaker/Quark/AutoRegLib"
        url = uri("https://maven.blamejared.com")
    }
    maven {
        name = "JEI"
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }
    maven {
        name = "Forgelin"
        url = uri("https://maven.shadowfacts.net/")
    }
    maven {
        name = "TOP"
        url = uri("https://maven.tterrag.com/")
    }
    maven {
        name = "Tinkers Construct"
        url = uri("https://dvs1.progwml6.com/files/maven")
    }
    maven {
        name = "TeamCoFH mods"
        url = uri("https://maven.covers1624.net/")
    }
    maven {
        name = "ChickenBones"
        url = uri("https://chickenbones.net/maven/")
    }
    maven {
        name = "Actually Additions"
        url = uri("https://maven.ellpeck.de/de/ellpeck/actuallyadditions/")
    }
    maven {
        name = "CurseMaven"
        url = uri("https://www.cursemaven.com")
    }
    maven {
        name = "IC2"
        url = uri("https://maven.ic2.player.to/")
    }
}

dependencies {
    minecraft(group = "net.minecraftforge", name = "forge", version = "1.12.2-14.23.5.2855")

    api (group = "org.jetbrains.kotlin", name = "kotlin-stdlib", version = "1.3.50")
    api (group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk7", version = "1.3.50")
    api (group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = "1.3.50")
    api (group = "org.jetbrains.kotlin", name = "kotlin-reflect", version = "1.3.50")

    implementation(fg.deobf(curse("enchantment_descriptions", 250419, 2689502)))
    implementation(fg.deobf(curse("enchantment_descriptions_sources", 250419, 2689503)))
    implementation(fg.deobf(curse("fluidlogged_api", 485654, 3384016)))
    compileOnly(fg.deobf(curse("dynamic_trees", 252818, 3260881)))
    compileOnly(curse("pams_harvestcraft", 221857, 2904825))
    compileOnly(fg.deobf(curse("plants", 257229, 2697165)))
    compileOnly(fg.deobf(curse("placebo", 283644, 2694382)))
    compileOnly(fg.deobf(curse("actually_additions", 228404, 2844115)))
    compileOnly(fg.deobf(curse("better_with_mods", 246760, 2965308)))
    compileOnly(fg.deobf(curse("better_with_lib", 294335, 2624990)))
    compileOnly(fg.deobf(curse("obfuscate", 289380, 2916310)))

    // todo fix this cause guy really uploaded a deobf library
    //compileOnly(fg.deobf("com.github.jbredwards:Fluidlogged-API:f5187ed7e3"))

    implementation("CraftTweaker2:CraftTweaker2-MC1120-Main:1.12-4.1.19.548")
    implementation(fg.deobf("mezz.jei:jei_1.12.2:4.15.0.+"))
    api("net.shadowfacts:Forgelin:1.8.4")
    compileOnly(fg.deobf("vazkii.quark:Quark:r1.6-180.7"))
    compileOnly(fg.deobf("vazkii.autoreglib:AutoRegLib:1.3-32.+"))
    compileOnly(fg.deobf("slimeknights.mantle:Mantle:1.12-1.3.3.49"))
    compileOnly(fg.deobf("slimeknights:TConstruct:1.12.2-2.13.0.184"))
    compileOnly(fg.deobf("cofh:CoFHCore:1.12.2-4.6.3.27:universal"))
    compileOnly(fg.deobf("cofh:CoFHWorld:1.12.2-1.3.1.7:universal"))
    compileOnly(fg.deobf("cofh:ThermalFoundation:1.12.2-2.6.3.27:universal"))
    compileOnly(fg.deobf("cofh:ThermalExpansion:1.12.2-5.5.4.43:universal"))
    compileOnly(fg.deobf("cofh:RedstoneFlux:1.12-2.1.0.7:universal"))
    compileOnly(fg.deobf("codechicken:CodeChickenLib:1.12.2-3.2.3.358:universal"))
    implementation(fg.deobf("mcjty.theoneprobe:TheOneProbe-1.12:1.12-1.4.28-17"))
}

tasks {
    jar {
        manifest {
            attributes(
                "Specification-Title" to "futuremc",
                "Specification-Vendor" to "thedarkcolour",
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to "thedarkcolour",
                "Implementation-Timestamp" to LocalDateTime.now(),
                "FMLCorePlugin" to "thedarkcolour.futuremc.asm.CoreLoader",
                "FMLCorePluginContainsFMLMod" to true,
                "FMLAT" to "futuremc_at.cfg"
            )
        }
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("mcmod.info") {
            expand("version" to project.version)
        }
    }

    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xinline-classes", "-Xjvm-default=enable")
            jvmTarget = "1.8"
            // wat
            //noReflect = false
            //noStdlib = false
        }
    }
}