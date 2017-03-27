# HashtagSearch - Version 2
Test for Applicaster, an automatically refreshing twitter search with persisted search history and search results. It works offline!

## Synopsis

After experimenting with using the Twitter Web API directly in HashtagSearch Version 1, I decided to use Fabric.io's excellent Twitter Kit.  After much research, I also discovered how to run Fabric with a custom OkHttp Client, and how to enable caching on it.  I  followed the Android developer guide for creating a searchable app.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Api Key Requirements

You'll need to have an account on [Fabric.io](https://fabric.io) and an [API key for twitter](https://dev.twitter.com/).  Twitter integration was done with the assistance of the [Fabric plugin for Android Studio](https://fabric.io/downloads/android)

```
Give examples
```

### Building the project

In order to build the app, you'll need to _create_ and populate the `app/fabric.properties` file.  The Fabric for Android plugin will help with this, and you can also refer to the [Fabric for Android sample app](https://github.com/twitter/twitter-kit-android/tree/master/samples).

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

### Setup

This project uses Espresso for instrument tests and Robolectric for unit tests.  Robolectric was only able to use Android SDK version 22 for testing.  Newer versions throw unrelated OpenGL errors.

To setup Robolectric, you'll need to create an *Android JUnit* configuration in Android Studio.  The key configuration is to set `Working directory` to `$MODULE_DIR$`

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

## Acknowledgments

* Thanks to Annyce Davis' [very neat and readable interceptors in samples for her book Adept Android](https://github.com/adavis/adept-android/tree/retrofit2-cache/app/src/main/java/info/adavis/adeptandroid)
* Thanks to Mike Nakhimovich's [better OkHttp header rewriting](https://github.com/digitalbuddha/StoreDemo/blob/master/app/src/main/java/com/digitalbuddha/daodemo/base/CacheInterceptor.java)
* Fabric's Twitter kit for Android excellent [sample app](https://github.com/twitter/twitter-kit-android/tree/master/samples)
* Stack Overflow example [reading and writing ArrayList to a file](http://stackoverflow.com/questions/12158483/how-to-write-an-arraylist-to-file-and-retrieve-it)
