import net.kyori.indra.IndraLicenseHeaderPlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintBasePlugin
import org.jlleitschuh.gradle.ktlint.KtlintIdeaPlugin

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
  alias(libs.plugins.indra)
  alias(libs.plugins.indra.license)
  alias(libs.plugins.indra.sonatype)
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.ktlint)
}

tasks.withType<AbstractPublishToMaven>() {
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
  apply<KtlintBasePlugin>()
  apply<KtlintIdeaPlugin>()

  repositories {
    mavenCentral()
  }

  dependencies {
    compileOnly(rootProject.project.libs.kotlin.stdlib)
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

    java {
      toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
      }
    }

    kotlin {
      explicitApi()
    }

    ktlint {
      version.set("0.45.1")
    }

    withType<KotlinCompile>() {
      kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
      }
    }
  }
}
