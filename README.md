# WeatherEventsApp
MSC Mobile App Project - Dylan French Carroll - 20080672



## Topics
 
+ [Firebase Authentication](#firebase-authentication)
+ [Firebase Realtime DB (CRUD)](#firebase-realtime-db-crud) 
+ [Notifications](#notifications-firebase-messagingfcm--cloud-functions)
+ [Notification Flow](#notification-flow)
+ [Firebase Cloud Functions (Notifications)](#firebase-cloud-functions)
+ [Firebase Messaging (FCM) (Notifications)](#firebase-messaging)
+ [Firebase Cloud Storage (Store Images for events)](#firebase-cloud-storage)
+ [Google Maps Display (Display events on map)](#google-maps-display)
+ [Nav Drawer Navigation](#nav-drawer-navigation)
+ [Custom Splashscreen](#custom-splash-screen)
+ [Display event details without Update or Delete functions](#display-event-details-without-update-or-delete-functions)
+ [Swipe to Update/Delete](#swipe-to-updatedelete)
+ [Scrollview Implemented on Some Fragments](#scrollview-implemented-on-some-fragments)
+ [Spinner Dropdown Element](#spinner-dropdown-element)
+ [Transitions Between Fragments](#transitions-between-fragments)
+ [Theme Switcher + Remember Preference for App](#theme-switcher--remember-preference-for-app)
+ [MVVM Pattern Followed from Labs](#mvvm-pattern-followed-from-labs)
+ [Code Linting/Refactor Applied](#code-lintingrefactor-applied)
+ [Git Approach](#git-approach)
+ [Class Diagram ](#class-diagram)
+ [Personal Statement]()
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
When creating an event, when the user adds in image and it is displayed the `Add Event` button may disappear off the screen due to the vertical constraints added to the button in regards to the `ImageView`. I implemented a scroll view so that a user can scroll down to view the `Add Event` button. Vertical constraints allows for some space between the bottom of the page and the `Add Event` button. <br /> <br /> 

### Spinner Dropdown Element
I create a drop down with hard coded event types that the user can select from. It caused some issue trying to have a `Hint` option as Android does not seem to have a simple way of achieving this goal. This Spinner dropdown can be seen on the `Add Event` page. <br /> <br /> 

### Transitions Between Fragments
I applied transitions between fragments using the a `nav_host` fragment. It is hard to notice these transitions as even with a very high spec PC, the emulator has an extremely low FPS or refresh rate. You can notice the `fade_in` and `fade_out` transitions when the emulator is responding in a speedy fashion. Check out `res/navigation/main_navigation.xml` to see them in the code. <br /> <br /> 

### Theme Switcher + Remember Preference for App
I created an item in the Nav Drawer to allow a use to choose between themes for the **application only**. The item can be clicked and a menu appears allowed the user to select `light`, `dark` or `System Default` theme. The change is applied and then the users preference is then saved. When the app is opened again and the user logs in, it will check for the saved theme preference and apply it. <br /> <br /> 

### MVVM Pattern Followed from Labs
I do not want to take too much credit for this work as after going through the labs I configured my project to use the same design approach and when adding new code I followed this pattern as best as I could. <br /> <br /> 

### Code Linting/Refactor Applied
I applied a code formatter to the entire code base after completion to ensure that the code did not have redundant white space and incorrect formatting as this can tend to happen when working on features and debugging. No one wants to look at unreadable code when grading. <br /> <br /> 

### Git Approach
<p>
I used a very similar approach to GIT as I do in my workplace. I created a `Develop` branch and managed all my dev work in there. I made many commits with bad messaging and a lot of small changes once a certain bug was added or part of a feature was working. Before `Pushing` my commits up to develop, I Squashed the 3/4 commits together and updated the commit message to be something more readable and concise for other developers to easily recognise what was added/fixed in each commit. <p/>
<p>
Once I was happy with my final work I created a pull request and Merged it to my Master branch as this is what other people will see when they open the repo. Most people would not track the develop branch and dont want updates for every commit i make on the develop branch. Once I was happy I created a Release and tagged it v1.0 as this was the base product I would want a user to install. Further development work would then have new releases when a feature was added e.g. v1.0.1 <p/>  <br /> <br /> 

### Class Diagram
Please take note of the class diagram (Generated by Intelli because Android Studeio does not have that feature). It is a rather large image, so I would suggest downloading the source image in the repo: `class_diagram.png`
![class_diagram.png](/class_diagram.png)<br /> <br /> 

### Personal Statement
<p>
This was one of the steeper learning curves I have had when it comes to development work. I had worked with Android in my Undergraduate previously, but after 3 or so years, my skills was very rusty. Conceptually I found this to differ a lot from other projects/work projects. It was hard to grasp the flow of the code and the patterns. UI work has always been the most difficult aspect of programming for myself due to a small lack of creativity. I found it espeically hard trying to create my own UI layout So I adopted a similar approach to UI elements in the labs. <p/>

<p>
Backend development I flew through as I could grasp the concepts very easily and come up with solutions. A somewhat complex solution of notifications came to me very easily and I did not have to spend a major amount of time on. I implemented many of the features that were specified in the assignment guide. <p/> 

<p>
 Overall the sturcture and layout of the course/labs provided me with an excellent base to build off and develop the application in the path I wanted. Many aspects were covered and the quality of the material was excellent. I cannot say I enjoyed Android development as it is not a passion of mine, but I cannot provide fault in the examples and structure of information provided to me via the course. <p/> 
 
 <p>
In terms of the project itself, I tended to lean further away from `new` UI work, instead I upgraded the logic around what I already had. Understanding my lack of skills in this area allowed me to implement more `network and server` logic to add features to my application <p/> 



