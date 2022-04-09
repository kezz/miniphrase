import com.diffplug.gradle.spotless.SpotlessPlugin
import com.diffplug.spotless.kotlin.KotlinConstants
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
  java
  alias(libs.plugins.indra)
  alias(libs.plugins.indra.sonatype)
  alias(libs.plugins.spotless)
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}

tasks.withType<AbstractPublishToMaven> {
  onlyIf { false }
}

indraSonatype {
  useAlternateSonatypeOSSHost("s01")
}

subprojects {
  apply<IndraPlugin>()
  apply<IndraPublishingPlugin>()
  apply<SpotlessPlugin>()

  repositories {
    mavenCentral()
  }

  tasks {
    indra {
      mitLicense()

      github("kezz", "miniphrase") {
        ci(true)
        issues(true)
      }

      configurePublications {
        pom {
          developers {
            developer {
              id.set("kezz")
              name.set("Kezz")
              url.set("https://kezz.dev")
            }
          }
        }
      }
    }

    spotless {
      // Custom header format that touches source files, enforcing a header update if the
      // file has changed when compared to upstream (i.e. only updating the copyright
      // year when the file has been updated).
      format("header") {
        target("**\\/*.java", "**\\/*.kt")
        ratchetFrom("origin/main")

        // Kotlin's license header delimiter matches Javas, essentially.
        licenseHeaderFile(
          rootProject.file("license_header.txt"),
          KotlinConstants.LICENSE_HEADER_DELIMITER
        )
      }

      java {
        googleJavaFormat("1.15.0")
      }
    }
  }
}
