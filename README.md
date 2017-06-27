Bintray deploy automator
===============
This project is a tool to publish artifacts to Bintray for gradle projects with multiple modules that use [Gradle Bintray Plugin](https://github.com/bintray/gradle-bintray-plugin).

Usage
-----
With this automator you just have to create a configuration file like the one below, build the jar and run it. It will clean and build your project and then for each module will run `bintrayUpload` task.
At the end if no errors occurred it will run the extra tasks from configuration.

Configuration
-------------
### Install
Run `jar` task from gradle and create configuration file.

### How to run
`java -jar BintrayDeployAutomator.jar -u Username -k Key`

### Configuration file (configuration.json)
The configuration file must be named `configuration.json`.
```js
// The json configuration
{
  "basePath": "./project", // The path of your project
  "readmePath": "./README.MD", // (Optional) If the project have a Readme with the actual version the automator can replace it
  "version": "0.1.9", // The new version of project to be uploaded
  // The list of modules to be uploaded to the bintray
  "modules": [
    "module-1",
    "module-2",
    "module-3"
  ],
  "extraTasks": [
      "task-1",
      "module-1:task-2"
  ]
}
```
### License
[MIT LICENSE](LICENSE.md)
