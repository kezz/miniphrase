dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
  }
}

rootProject.name = "miniphrase-parent"

sequenceOf(
  "core"
).forEach { projectName ->
  include("miniphrase-$projectName")
  project(":miniphrase-$projectName").projectDir = file(projectName)
}
