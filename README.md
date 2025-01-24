# Activity Art

## Summary
This Android application uses the Strava API to enable athletes to generate aesthetic, highly-modular visualizations of their workouts fit for digital printing. An athlete might only want to use some of their activities, so they may use various filters including date, type, and distance. The artwork may be styled by changing the background, activities, or text colors to the athletes' desire and with customizable typography, including a diverse selection of fonts. The generated art may be set to whichever aspect ratio and resolution the athletes require for the particular print size needed.

An athlete may download or share the artwork after they are finished customizing it.

<a href='https://play.google.com/store/apps/details?id=com.activityartapp'><img height="75"  alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>

## Android Technologies

* Jetpack Compose utilizing MVVM architecture
* Dagger Hilt
* Retrofit
* [![Build Status](https://app.bitrise.io/app/329fbd21a19fa0ff/status.svg?token=PrBFv8aiU3XJ4EJoG1AIpg&branch=master)](https://app.bitrise.io/app/329fbd21a19fa0ff)


## Rationing API Usage
To mitigate how restrictive Strava is on the third-party usage of their API, a strategy of caching activities to the device and tracking athlete usage on a remote Firebase Realtime Database is employed. Should an athlete's usage exceed the maximum allotted, they will receive an error message that they may either make artwork with cached activities or try again later.
Athlete usage is cleared twice daily by the following Cloud Function.

```typescript
import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

admin.initializeApp();

export const scheduledClearAthleteUsage = functions
    .pubsub
    .schedule("every 720 minutes")
    .onRun((context) => {
      const dbRef = admin.database().ref("athlete_usage");
      dbRef.remove();
      return null;
    });
```

## Ensuring App Version is the Latest and is Supported
To ensure that the client's application is the latest version and supported, a Firestore collection is queried. If the app is unsupported, athletes are redirected to a screen that prohibits further action until they update. Alternatively, if the app is not the latest version but is supported, a message appears informing them. 

This is a critical function to ensure that we may disable the usage of older versions of the application in a user-friendly manner.

## References
https://developers.strava.com/docs/authentication/<br>
https://developers.strava.com/guidelines/<br>
https://medium.com/@davethomas_9528/please-dont-use-singletons-to-persist-state-on-android-7bac9bc78b29<br>
