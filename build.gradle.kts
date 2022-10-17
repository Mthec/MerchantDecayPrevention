plugins {
    java
}

group = "mod.wurmunlimited.npcs.merchantdecayprevention"
version = "0.1.1"
val shortName = "merchantdecayprevention"
val wurmServerFolder = "F:/Steam/steamapps/common/Wurm Unlimited/WurmServerLauncher/"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation(project(":WurmTestingHelper"))
    implementation("com.wurmonline:server:1.9")
    implementation("org.gotti.wurmunlimited:server-modlauncher:0.45")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    jar {
        doLast {
            copy {
                from(jar)
                into(wurmServerFolder + "mods/" + shortName)
            }

            copy {
                from("src/main/resources/$shortName.properties")
                into(wurmServerFolder + "mods/")
            }
        }

        includeEmptyDirs = false
        archiveFileName.set("$shortName.jar")

        manifest {
            attributes["Implementation-Version"] = version
        }
    }

    register<Zip>("zip") {
        into(shortName) {
            from(jar)
        }

        from("src/main/resources/$shortName.properties")
        archiveFileName.set("$shortName.zip")
    }
}
