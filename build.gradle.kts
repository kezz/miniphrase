import net.kyori.indra.IndraLicenseHeaderPlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
  alias(libs.plugins.indra)
  alias(libs.plugins.indra.license)
  alias(libs.plugins.indra.sonatype)
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.ktlint)
}

tasks.withType<AbstractPublishToMaven> {
  onlyIf { false }
}

indraSonatype {
  useAlternateSonatypeOSSHost("s01")
}

subprojects {
  apply<IndraPlugin>()
  apply<IndraLicenseHeaderPlugin>()
  apply<IndraPublishingPlugin>()
  apply<KotlinPluginWrapper>()
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

  dependencies {
    compileOnly(rootProject.project.libs.kotlin.stdlib)
  }

  kotlin {
    jvmToolchain {
      languageVersion.set(JavaLanguageVersion.of(21))
    }

    jvmToolchain(17)
    explicitApi()
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

    ktlint {
      version.set("1.0.1")
    }

    withType<KotlinCompile> {
      kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
      }
    }
  }
}
