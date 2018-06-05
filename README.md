See our site for better context of this readme. [Click here](http://atmosphereframework.com/)

# atmosphere-integration-tests
These are the integration tests of the ATMOSPHERE mobile testing framework.

## Project setup
>This project depends on:
* [atmosphere-agent](https://github.com/MusalaSoft/atmosphere-agent)
* [atmosphere-client](https://github.com/MusalaSoft/atmosphere-client)
* [atmosphere-server](https://github.com/MusalaSoft/atmosphere-server)
* [atmosphere-agent-device](https://github.com/MusalaSoft/atmosphere-agent-device-lib)
* [atmosphere-commons](https://github.com/MusalaSoft/atmosphere-commons)
* [atmosphere-client-server](https://github.com/MusalaSoft/atmosphere-client-server-lib)
* [atmosphere-server-agent](https://github.com/MusalaSoft/atmosphere-server-agent-lib)
* [atmosphere-ime](https://github.com/MusalaSoft/atmosphere-ime)
* [atmosphere-service](https://github.com/MusalaSoft/atmosphere-service)
* [atmosphere-uiautomator-bridge](https://github.com/MusalaSoft/atmosphere-uiautomator-bridge/)

> Make sure you publish these projects to your local Maven repository (follow each project's setup instructions).

### Run the tests
Make sure you have connected at least one Android device (a physical device or an emulator) to the machine you will be running the tests on. Then run the tests using the included Gradle wrapper with the following command:
* `./gradlew build` on Linux/macOS
* `gradlew build` on Windows.
