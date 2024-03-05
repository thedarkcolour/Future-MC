plugins {
    java
    idea
    kotlin("jvm") version "1.9.0" // To fix broken Gradle + syntax
    id("com.gtnewhorizons.retrofuturagradle") version "1.3.16"
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))

version = "0.2.19"
group = "thedarkcolour.futuremc"

minecraft {
    mcVersion.set("1.12.2")
    mcpMappingChannel.set("stable")
    mcpMappingVersion.set("39")
    username.set("Dev")
    useDependencyAccessTransformers.set(true)
    extraRunJvmArguments.add("-Dfml.coreMods.load=thedarkcolour.futuremc.asm.CoreLoader")
}

val targetFile = "src/main/resources/META-INF/futuremc_at.cfg"

tasks.deobfuscateMergedJarToSrg.get().accessTransformerFiles.from(targetFile)
tasks.srgifyBinpatchedJar.get().accessTransformerFiles.from(targetFile)

repositories {
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
        content {
            includeGroup("codechicken")
        }
    }
    //maven {
    //    name = "Actually Additions"
    //    url = uri("https://maven.ellpeck.de/de/ellpeck/actuallyadditions/")
    //}
    maven {
        name = "CurseMaven"
        url = uri("https://www.cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
    //maven {
    //    name = "IC2"
    //    url = uri("https://maven.ic2.player.to/")
    //    content {
    //        includeGroup("")
    //    }
    //}
    //maven {   // Worldedit API
    //    url = uri("https://maven.sk89q.com/repo/")
    //    content { includeGroup("") }
    //}
    //maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation (group = "org.jetbrains.kotlin", name = "kotlin-stdlib", version = "1.3.50")
    implementation (group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk7", version = "1.3.50")
    implementation (group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = "1.3.50")
    implementation (group = "org.jetbrains.kotlin", name = "kotlin-reflect", version = "1.3.50")

    // Stuff I care about
    curseMaven("enchantment_descriptions", 250419, 2689502)
    curseMaven("enchantment_descriptions_sources", 250419, 2689503)
    curseMaven("fluidlogged_api", 485654, 3698755)
    curseMaven("biomes_o_plenty", 220318, 2842510)
    implementation("CraftTweaker2:CraftTweaker2-MC1120-Main:1.12-4.1.19.548")
    curseMaven("had-enough-items", 557549, 4810661, runtime = true)
    api("net.shadowfacts:Forgelin:1.8.4")

    // OTG
    curseMaven("otg", 265894, 3151431)

    // Optional mod compat
    curseMaven("dynamic_trees", 252818, 3260881)
    curseMaven("pams_harvestcraft", 221857, 2904825)
    curseMaven("plants", 257229, 2697165)
    curseMaven("placebo", 283644, 2694382)
    curseMaven("actually_additions", 228404, 2844115)
    curseMaven("better_with_mods", 246760, 2965308)
    curseMaven("better_with_lib", 294335, 2624990)
    curseMaven("obfuscate", 289380, 2916310)
    curseMaven("carryon", 274259, 4507139, runtime = true)
    compileOnly(rfg.deobf("vazkii.quark:Quark:r1.6-180.7"))
    compileOnly(rfg.deobf("vazkii.autoreglib:AutoRegLib:1.3-32.+"))
    compileOnly(rfg.deobf("slimeknights.mantle:Mantle:1.12-1.3.3.49"))
    compileOnly(rfg.deobf("slimeknights:TConstruct:1.12.2-2.13.0.184"))
    compileOnly(rfg.deobf("cofh:CoFHCore:1.12.2-4.6.3.27:universal"))
    compileOnly(rfg.deobf("cofh:CoFHWorld:1.12.2-1.3.1.7:universal"))
    compileOnly(rfg.deobf("cofh:ThermalFoundation:1.12.2-2.6.3.27:universal"))
    compileOnly(rfg.deobf("cofh:ThermalExpansion:1.12.2-5.5.4.43:universal"))
    compileOnly(rfg.deobf("cofh:RedstoneFlux:1.12-2.1.0.7:universal"))
    compileOnly(rfg.deobf("codechicken:CodeChickenLib:1.12.2-3.2.3.358:universal"))
    implementation(rfg.deobf("mcjty.theoneprobe:TheOneProbe-1.12:1.12-1.4.28-17"))
    curseMaven("oe", 840576, 4670168, runtime = true)
}

fun DependencyHandlerScope.curseMaven(modName: String, projectId: Int, fileId: Int, runtime: Boolean = false) {
    val dep = rfg.deobf("curse.maven:$modName-$projectId:$fileId")
    if (runtime) {
        implementation(dep)
    } else {
        compileOnly(dep)
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("mcversion", project.minecraft.mcVersion)

        // Replace various properties in mcmod.info and pack.mcmeta if applicable
        filesMatching(listOf("mcmod.info", "pack.mcmeta")) {
            // Replace version and mcversion
            expand(mapOf("version" to project.version, "mcversion" to project.minecraft.mcVersion))
        }
    }

    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xinline-classes", "-Xjvm-default=all")
            jvmTarget = "1.8"
            languageVersion = "1.4"
            apiVersion = "1.4"
        }
    }

    jar {
        manifest {
            attributes["FMLAT"] = "futuremc_at.cfg"
            attributes["FMLCorePlugin"] = "thedarkcolour.futuremc.asm.CoreLoader"
            attributes["FMLCorePluginContainsFMLMod"] = "true"
        }
    }
}
