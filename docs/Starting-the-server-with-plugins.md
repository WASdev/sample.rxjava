You can use the [Gradle](#running-with-gradle) to control and manipulate the server for use in automated builds to support continuous integration.

## Running with Gradle

The [Gradle plugin](https://github.com/WASdev/ci.gradle) can manage the server using the following operations:

* TODO: more here. I know, you're impatient. Stop gnashing your teeth at me.

## Additional Notes

:star: *Note:* The Gradle clean task will clean server output (logs and workarea, etc) from the rxjava-wlpcfg directory, however, if you wanted to maintain strict separation between what is checked into rxjava-wlpcfg and what is generated by a running server, you could also specify the WLP_OUTPUT_DIR environment variable, e.g. into the Gradle target directory.

```bash
$ export WLP_OUTPUT_DIR=${WLP_USER_DIR}/target
```
