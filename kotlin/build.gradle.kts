import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.ktlint)
}

dependencies {
  api(projects.miniphraseCore)

  compileOnly(rootProject.project.libs.kotlin.stdlib)
}

tasks {
  kotlin {
    explicitApi()
  }

  ktlint {
    // We can't use Spotless here because ktlint doesn't read from .editorconfig but
    // at the same time doesn't let you override anything that can be defined in a
    // .editorconfig file. We also can't backdate as we need the latest version in order
    // to use context receivers without them throwing a formatting error. Fun, eh?
    // https://github.com/diffplug/spotless/issues/142
    version.set("0.45.2")
  }

  withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
  }
}
