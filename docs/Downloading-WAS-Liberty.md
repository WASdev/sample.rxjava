There are lots of ways to get your hands on WAS Liberty. 

To download just the WAS Liberty runtime, go to the [wasdev.net Downloads page][wasdev], and choose between the [latest version of the runtime][wasdev-latest] or the [latest beta][wasdev-beta]. You can also download Liberty via [Eclipse and WDT](/docs/Downloading-WAS-Liberty.md).

There are a few options to choose from (especially for the beta drivers). Choose the one that is most appropriate.
* There are convenience archives for downloading pre-defined content groupings
* You can add additional features from the repository using the [installUtility][installUtility] or the [Gradle][gradle-plugin] plugins.

Note that for this particular sample, you will need a version of Liberty that has support for WebSockets 1.1, CDI 1.2, and Concurrency Utilities 1.0, as it has a little bit of all of those.   

While the Java EE 7 Full Platform has all of these, a more lightweight approach would be to install the Java EE 7 Web Profile, and then to add on the ***concurrency-1.0*** feature through either the [installUtility][installUtility] or [Gradle][gradle-plugin] plugins, or through the WDT **Install Add-ons** wizard when creating or updating a Liberty runtime environment.

[wasdev]: https://developer.ibm.com/wasdev/downloads/
[wasdev-latest]: https://developer.ibm.com/wasdev/downloads/liberty-profile-using-non-eclipse-environments/
[wasdev-beta]: https://developer.ibm.com/wasdev/downloads/liberty-profile-beta/
[installUtility]: http://www-01.ibm.com/support/knowledgecenter/#!/was_beta_liberty/com.ibm.websphere.wlp.nd.multiplatform.doc/ae/rwlp_command_installutility.html
[gradle-plugin]: https://github.com/WASdev/ci.gradle

## Tips

* If you use bash, consider trying the [command line tools](https://github.com/WASdev/util.bash.completion), which provide tab-completion for the server and other commands.
