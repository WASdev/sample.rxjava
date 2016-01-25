## Building and running the sample using the command line

### Clone Git Repo
:pushpin: [Switch to Eclipse example](/docs/Using-WDT.md/#clone-git-repo)

```bash

$ git clone https://github.com/WASdev/sample.rxjava.git

```

### Building the sample
:pushpin: [Switch to Eclipse example](/docs/Using-WDT.md/#building-the-sample-in-eclipse)

This sample can be built using [Gradle](#gradle-commands).

###### [Gradle](http://gradle.org/) commands

```bash
$ gradle build publishToMavenLocal
```

If you want to also run the functional tests then you need to [Download WAS Liberty](/docs/Downloading-WAS-Liberty.md) and set the libertyRoot property in the gradle.properties file to point to your Liberty install.

In addition to publishing the war to the local maven repository, the built war file is copied into the apps directory of the server configuration located in the rxjava-wlpcfg directory:

```text
rxjava-wlpcfg
 +- servers
     +- rxjavaSample                           <-- specific server configuration
        +- server.xml                          <-- server configuration
        +- apps                                <- directory for applications
           +- rxjava-application.war           <- sample application
        +- logs                                <- created by running the server locally
        +- workarea                            <- created by running the server locally
```

### Running the application locally
:pushpin: [Switch to Eclipse example](/docs/Using-WDT.md/#running-the-application-locally)

Pre-requisite: [Download WAS Liberty](/docs/Downloading-WAS-Liberty.md)

Use the following to start the server and run the application:

```bash
$ export WLP_USER_DIR=/path/to/sample.rxjava/rxjava-wlpcfg
$ /path/to/wlp/bin/server run rxjavaSample
```

* `run` runs the server in the foreground.
* `start` runs the server in the background. Look in the logs directory for console.log to see what's going on, e.g.

```bash
$ tail -f ${WLP_USER_DIR}/servers/rxjavaSample/logs/console.log
```
