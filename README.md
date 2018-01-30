# Bintray deploy automator

This tool to publish artifacts to Bintray for gradle projects with multiple modules that use [Gradle Bintray Plugin](https://github.com/bintray/gradle-bintray-plugin) or [Novoda Bintray Release](https://github.com/novoda/bintray-release).

## How it works

Bintray deploy automator clean and build your project's modules and then for each module will run `bintrayUpload` task. At the end if no errors occurred it will run the extra tasks.

## How to run

1. Create a configuration file named `configuration.json` like the one below.

```json
{
  "basePath": "./project", // The path of your project
  "version": "0.0.5", // The new version of project to be uploaded
  // The list of modules to be uploaded to the bintray
  "modules": [
    "module-2",
    "module-3"
  ],
  // Extra tasks
  "extraTasks": [
      "task-1",
      "module-1:task-2"
  ]
}
```

2. Download the latest [release](https://github.com/massivedisaster/bintray-deploy-automator/releases).
3. Execute `java -jar BintrayDeployAutomator-0.0.5.jar -u Username -k Key`

### Generate release JAR

Run `jar` task from gradle.

### License

[MIT LICENSE](LICENSE.md)
