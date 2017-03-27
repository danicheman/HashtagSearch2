# HashtagSearch - Version 2
This test project for Applicaster, an automatically refreshing Twitter search with persisted history, provides Tweet results from the 10 most influential people.

## Synopsis

In designing the search interface, the best practices from the Android Developers site were followed. The app allows you to configure the frequency for updating search results. After investigating Twitter4j, and interacting with Twitter's Web API directly in HashtagSearch Version 1, I ultimately decided to use [Fabric's](https://fabric.io) excellent Twitter Kit.  

Twitter's [Fabric sample app](https://github.com/twitter/twitter-kit-android/tree/master/samples), demonstrates the simplest way to run Fabric with a custom OkHttp client.  By combining several interceptor examples I was able to create an effective caching component for my custom OkHttp client.

## Getting Started

### Api Key Requirements

You'll need to have an account on [Fabric.io](https://fabric.io) and an [API key for Twitter](https://dev.twitter.com/).  Twitter integration was done with the assistance of the [Fabric plugin for Android Studio](https://fabric.io/downloads/android)

### Building the project

With Api keys now available, to build the app, you'll need to *create* and *populate those keys* into the `app/fabric.properties` file.  The Fabric for Android plugin will help with this, and you can also refer to the [Fabric for Android sample app](https://github.com/twitter/twitter-kit-android/tree/master/samples).

Sample [fabric.properties](https://raw.githubusercontent.com/twitter/twitter-kit-android/master/samples/app/fabric.properties.sample) file:

```
#These keys are provisioned on the plugin. Copy the keys on the plugin here.
twitterConsumerKey=EDIT_THIS
twitterConsumerSecret=EDIT_THIS

#Contains API Secret used to validate your application. Commit to internal source control; avoid making secret public.
apiKey=EDIT_THIS
apiSecret=EDIT_THIS
```

## Testing

### Unit tests with Robolectric

This project uses Robolectric for unit tests.  Robolectric uses Android SDK version 22 for testing because newer versions throw unrelated OpenGL errors.

To setup Robolectric, you'll need to create a new *Android JUnit* configuration in Android Studio:

* Set `Test Kind:` to `All in package:`
* Set `Package:` to `com.actest.nick.hashtagsearch2`
* Set `Search for tests:` to `In single module`
* Set `Working directory` to `$MODULE_DIR$`. 

### Instrument tests with Espresso

Espresso instrument tests require that you have either an Android device attached or an emulator running.  Once you have that, run a test by command-clicking/right-clicking
a specific instrument test and choosing run.  The run configuration will automatically be created and the test will be launched.

## Acknowledgments

* Thanks to Annyce Davis' [very neat and readable interceptors in samples for her book Adept Android](https://github.com/adavis/adept-android/tree/retrofit2-cache/app/src/main/java/info/adavis/adeptandroid)
* Thanks to Mike Nakhimovich's [better OkHttp header rewriting](https://github.com/digitalbuddha/StoreDemo/blob/master/app/src/main/java/com/digitalbuddha/daodemo/base/CacheInterceptor.java)
* Fabric's Twitter kit for Android excellent [sample app](https://github.com/twitter/twitter-kit-android/tree/master/samples)
* Stack Overflow example [reading and writing ArrayList to a file](http://stackoverflow.com/questions/12158483/how-to-write-an-arraylist-to-file-and-retrieve-it)
