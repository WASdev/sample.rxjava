## Eclipse / WDT

The WebSphere Development Tools (WDT) for Eclipse can be used to control the server (start/stop/dump/etc.), it also supports incremental publishing with minimal restarts, working with a debugger to step through your applications, etc.

WDT also provides:

* content-assist for server configuration (a nice to have: server configuration is minimal, but the tools can help you find what you need and identify finger-checks, etc.)
* automatic incremental publish of applications so that you can write and test your changes locally without having to go through a build/publish cycle or restart the server (which is not that big a deal given the server restarts lickety-split, but less is more!).

Installing WDT on Eclipse is as simple as a drag-and-drop, but the process is explained [on wasdev.net] [wasdev-wdt].

[wasdev-wdt]: https://developer.ibm.com/wasdev/downloads/liberty-profile-using-eclipse/

### Clone Git Repo
:pushpin: [Switch to cmd line example](/docs/Using-cmd-line.md/#clone-git-repo)

If the sample git repository hasn't been cloned yet, WDT has git tools integrated into the IDE:

1.  Open the Git repositories view
    * *Window -> Show View -> Other*
    * Type "git" in the filter box, and select *Git Repositories*
2.  Copy Git repo url by finding the textbox under "HTTPS clone URL" at the top of this page, and select *Copy to clipboard*
3.  In the Git repositories view, select the hyperlink `Clone a Git repository`
4.  The git repo url should already be filled in.  Select *Next -> Next -> Finish*
5.  The "sample.rxjava [master]" repo should appear in the view

### Building the sample in Eclipse
:pushpin: [Switch to cmd line example](/docs/Using-cmd-line.md/#building-the-sample)

This sample can be built using [Gradle](#building-with-gradle).

#### Building with [Gradle](http://gradle.org/)

###### Import Gradle projects into WDT

This assumes you have the Gradle [Buildship](https://projects.eclipse.org/projects/tools.buildship) tools installed into Eclipse Mars.

1. In the Git Repository view, expand the rxjava repo to see the "Working Directory" folder
2. Right-click on this folder, and select *Copy path to Clipboard*
3. Select menu *File -> Import -> Gradle -> Gradle Project*
4. In the *Project root directory* folder textbox, Paste in the repository directory.
5. Click *Next* twice
6. Three projects should be listed in the *Gradle project structure* click *Finish*
7. This will create 3 projects in Eclipse: sample.rxjava, rxjava-application, and rxjava-wlpcfg
8. Go to the *Gradle Tasks* view in Eclipse and navigate to the *sample.rxjava* project
9. Double click on the *eclipse* task to generate all the Eclipse files
10. In the *Enterprise Explorer* view in Eclipse right click on the three projects mentioned in step 7 and click refresh

:star: *Note:* If you did not use Eclipse/WDT to clone the git repository, follow from step 3, but navigate to the cloned repository directory rather than pasting its name in step 4.

###### Run Gradle build

1. Right-click on rxjava-application/build.gradle
2. *Run As > Gradle Build...*
3. In the *Gradle Tasks* section enter "build"
4. Click *Run*

### Running the application locally
:pushpin: [Switch to cmd line example](/docs/Using-cmd-line.md/#running-the-application-locally)

Pre-requisite: [Download WAS Liberty](docs/Downloading-WAS-Liberty.md)

For the purposes of this sample, we will create the Liberty server (step 3 in the wasdev.net instructions) a little differently to create and customize a Runtime Environment that will allow the server to directly use the configuration in the `rxjava-wlpcfg` project.

###### Create a Runtime Environment in Eclipse

1. Open the 'Runtime Explorer' view:
    * *Window -> Show View -> Other*
    * type `runtime` in the filter box to find the view (it's under the Server heading).
2. Right-click in the view, and select *New -> Runtime Environment*
3. Give the Runtime environment a name, e.g. `wlp-2016.1.0.0` if you're using the January 2016 beta.
4. Either:
    * Select an existing installation (perhaps what you downloaded earlier, if you followed those instructions), or
    * select *Install from an archive or a repository* to download a new Liberty archive.
5. Follow the prompts (and possibly choose additional features to install) until you *Finish* creating the Runtime Environment

###### Add the User directory from the Gradle project, and create a server

1. Right-click on the Runtime Environment created above in the 'Runtime Explorer' view, and select *Edit*
2. Click the `Advanced Options...` link
3. If the `rxjava-wlpcfg` directory is not listed as a User Directory, we need to add it:
    1. Click New
    2. Select the `rxjava-wlpcfg` project
    3. Select *Finish*, *OK*, *Finish*
4. Right-click on the `rxjava-wlpcfg` user directory listed under the target Runtime Environment in the Runtime Explorer view, and select *New Server*.
5. The resulting dialog should be pre-populated with the `rxjavaSample` Liberty profile server.
   The default name for this server can vary, you might also opt to rename it from the Right-click menu in the Servers view to make it easier to identify.
6. Click *Finish*

###### Running Liberty and the sample application from WDT

1.  Select the `rxjava-application` project
2.  Right-click -> *Run As... -> Run On Server*
3.  Select the appropriate server (as created above) and select *Finish*
4.  Confirm that a WebSocket connection can be opened to the "ws://localhost:9080/rxjava-application/"

:star: *Note:* Some versions of WDT incorrectly map the cdi-1.2 dependency to the CDI 1.0 Facet, which prevents the *Run As ...* operation in step 2 from succeeding. If this happens, Right-click on the `rxjava-application` project, and select *Properties*, then select *Project Facets* in the left-hand pane. Change the the "Context and dependency injection (CDI)" facet to use version 1.2, at which point, step 2 (above) should work.