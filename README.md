# WeatherEventsApp
MSC Mobile App Project - Dylan French Carroll - 20080672



## Topics
 
+ Firebase Authentication
+ Firebase Realtime DB (CRUD) 
+ Firebase Cloud Functions (Notifications)
+ Firebase Messaging (FCM) (Notifications)
+ Firebase Cloud Storage (Store Images for events)
+ Google Maps display (Display events on map)
+ Nav Drawer navigation
+ Custom Splashscreen 
+ Display event details without Update or Delete functions
+ Swipe to update/delete
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

I used Firebase Authentcation to authenticate users within my application. Users can create an account with an arbitrary email/password. The email does not require verification as I dont want users to provide their actual email for demo purposes. Any combination of email/password can be used without need of an actual email address. Users can also sign in via Google to ease this process. <br /> 

### Firebase Realtime DB (CRUD)
I choose the older Realtime Database to store events for my app. Users have access to CRUD functionality once they sign in. Users can delete and update events by clicking on them from the report page or on the event within the maps screen. This is all very rudimentary principles and I feel like I do not need to go too indepth on this topic as some of the specific features are explained below. <br /> 

### Notifications (Firebase Messaging(FCM) + Cloud Functions)  
To achieve my goal of sending notifications to every device once an event is created, I decided to implement Firebase Messaging and Cloud Functions.  
