rootProject.name = "miniphrase-parent"

sequenceOf(
  "core",
  "kotlin"
).forEach { projectName ->
  include("miniphrase-$projectName")
  project(":miniphrase-$projectName").projectDir = file(projectName)
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
