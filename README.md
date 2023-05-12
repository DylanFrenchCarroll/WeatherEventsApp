# WeatherEventsApp
MSC Mobile App Project - Dylan French Carroll - 20080672



## Topics
 
+ Firebase Authentication
+ Firebase Realtime DB (CRUD) 
+ Notifications
+ Notification Flow
+ Firebase Cloud Functions (Notifications)
+ Firebase Messaging (FCM) (Notifications)
+ Firebase Cloud Storage (Store Images for events) 
+ Google Maps Display (Display events on map)
+ Nav Drawer Navigation
+ Custom Splashscreen 
+ Display event details without Update or Delete functions
+ Swipe to Update/Delete
+ Scrollview Implemented on Some Fragments
+ Spinner Dropdown Element
+ Transitions Between Fragments
+ Theme Switcher + Remember Preference for App
+ MVVM Pattern Followed from Labs
+ Code Linting/Refactor Applied
+ Git Approach
+ Class Diagram 
+ Personal Statement
<br /> 

### Firebase Authentication

I used Firebase Authentcation to authenticate users within my application. Users can create an account with an arbitrary email/password. The email does not require verification as I dont want users to provide their actual email for demo purposes. Any combination of email/password can be used without need of an actual email address. Users can also sign in via Google to ease this process. <br /> <br /> 

### Firebase Realtime DB (CRUD)
I choose the older Realtime Database to store events for my app. Users have access to CRUD functionality once they sign in. Users can delete and update events by clicking on them from the report page or on the event within the maps screen. This is all very rudimentary principles and I feel like I do not need to go too indepth on this topic as some of the specific features are explained below. <br /> <br /> 

### Notifications (Firebase Messaging(FCM) + Cloud Functions)  
To achieve my goal of sending notifications to every device once an event is created, I decided to implement Firebase Messaging and Cloud Functions. <br /> 

#### Notification Flow
+ When a user logs in, the device is subbed to a topic `weatherEvents` in FCM
+ Then user will create an event via the app
+ The application with create the entry in the database
+ The Cloud Function has a listener set up to detect `writes` to the realtime database document `/events`
+ Once the listener detects a new write it will read the event that has been wrtitten 
+ It then uses Firebase Admin to send a message to a topic in the Firebase Messaging (FCM) 
+ FCM will then send a notification to every device that is subscriped to the topic `weatherEvents` 
+ The notification will appear on the device if the application is not open

#### Firebase Cloud Functions
You can check out the cloud function that I uploaded to Firebase in the `cloud_functions` directory in the root of this repo. The function is specified in `cloud_functions/functions/indexjs`. The function makes use of Firebase Admin SDK to listen to writes to the `/events` document. Once a write is detected it then creates a notification payload and a hardcoded topic ("weatherEvents") to publish it to. The admin SDK then sends the message to the topic. This is a pub-sub type service. I was required to enabled feature for my API in google console  <br/>

#### Firebase Messaging 
I used Firebase Cloud Messaging to send notification rather than create my own pub/sub service and host it online. Once a user has logged into the app their device is subbed to a topic e.g. `subscribeToTopic("weatherEvents")`. This is registered with FCM and FCM can now publish notifications to be received by the device when the application is not in focus. They will not appear when the app is open. To implement this into my project, I added it via the `Tools -> Firebase` menu. I then ran the `FirebaseDBService.subToTopic()` function once the `Home` activity was started. I was required to enabled feature for my API in google console <br /> <br /> 

### Firebase Cloud Storage
I configured Firebase Cloud storage to store images of events that are uploaded for every event. The URI of the image is then stored in the Realtime Database as a string and converted to URI when needed within the app. As the upload of an image takes some time, I have some checks in place to ensure that the image was uploaded and the event was written to the database before we pop back to the report page to ensure consistency of data in the report list. I was required to enabled feature for my API in google console <br/> <br/>

### Google Maps Display
I have implemented Google Maps functionality for my application. When a user adds an event it takes their current location and saves the latitude and longitude in the event data. A user can view all of their and every other notification on the map via a tab on the Nav Drawer. A user can also click on a pin on the map to view/edit/delete details of their events. They can also click events from other users and open it in a `read only view` where they can only view not edit/delete the event. I was required to enabled feature for my API in google console <br /> <br /> 

### Nav Drawer Navigation
I used a Nav Drawer to navigate through my application. It also provides some extra options such as sign out, change profile picture, change themes and view the `about us` page. These will be discussed later in this document as will the menu navigation. <br /> <br /> 

### Custom Splash Screen 
I created a custom splash screen to display when the application is loading. It contains information including the app name, my name/student number and the W.I.T. logo (Not S.E.T.U) <br /> <br /> 

### Display event details without Update or Delete functions
In the labs we could not view details of donations that were created by other users. I decided to create a new view (`readOnlyEvent`) and changed the functionality in the app to display the event in a read only view so that more information could be seen by the user. Instead of updating the logic in the already existing `donationDetail` I decided to show that I could create a new fragment and functionality for this feature. <br /> <br /> 

### Swipe to Update/Delete
I created Swipe to Update/Delete events functionality in this app. It largely follows the work done within the labs but it is useful functionality to include in my application. Not much more to discuss on this topic as I dont want to pawn the idea off as my own. <br /> <br /> 

### Scrollview Implemented on Some Fragments
When creating an event, when the user adds in image and it is displayed the `Add Event` button may disappear off the screen due to the vertical constraints added to the button in regards to the `ImageView`. I implemented a scroll view so that a user can scroll down to view the `Add Event` button. Vertical constraints allows for some space between the bottom of the page and the `Add Event` button
