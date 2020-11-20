plugins {
    java
}

group = "mod.wurmunlimited.npcs.merchantdecayprevention"
version = "0.1"
val shortName = "merchantdecayprevention"
val wurmServerFolder = "E:/Steam/steamapps/common/Wurm Unlimited/WurmServerLauncher/"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(project(":WurmTestingHelper"))
    implementation(fileTree(wurmServerFolder) { include("server.jar") })
    implementation(fileTree(wurmServerFolder + "lib/") { include("ServerLauncher-0.43.jar",
            "sqlite-jdbc-3.8.11.2.jar", "WurmUnlimitedCommon-1.9.2.7.jar") })
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
