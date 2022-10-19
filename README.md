![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/skycavemc/skycavelib?include_prereleases&style=plastic)
![GitHub license](https://img.shields.io/github/license/skycavemc/skycavelib?style=plastic)
[![CodeQL](https://github.com/skycavemc/skycavelib/actions/workflows/codeql.yml/badge.svg)](https://github.com/skycavemc/skycavelib/actions/workflows/codeql.yml)
# skycavelib
Library for SkyCave plugins.
For using  or publishing this library, you need a personal access token with access rights to this repository.

## Local setup
In order for the package publishing to work, you need to set up a `local.properties` file:
```properties
gpr.user=
gpr.key=
```
The rest is already configured in `build.gradle`.

## Using the library in another project
- Create a `local.properties` file as explained above.
- First, you have to read the properties from `local.properties` in your `build.gradle` file:
  ```groovy
  import java.util.Properties
  import java.io.FileInputStream
  
  val localProperties = new Properties()
  localProperties.load(FileInputStream(rootProject.file("local.properties")))
  ```
- Now you can add the repository as follows:
  ```groovy
  repositories {
      maven {
          url = uri("https://maven.pkg.github.com/skycavemc/skycavelib")
          credentials {
              username = localProperties.getProperty("gpr.user")
              password = localProperties.getProperty("gpr.key")
          }
      }
  }
  ```
- Finally, you can add the dependency (_don't forget to replace `VERSION` with your desired version_):
  ```groovy
  dependencies {
       implementation("de.skycave:skycavelib:VERSION")
  }
  ```
