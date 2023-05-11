const {
    log,
    info,
    debug,
    warn,
    error,
    write,
  } = require("firebase-functions/logger");
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// exports.sendListenerPushNotification  =  functions.database.ref('/events/{pushId}').onWrite(event => {
// const topic = "weatherEvents"
// const msgEvent= event.after.val();

//      const payload = {notification: {
//          title: `${msgEvent.eventtype}`,
//          body: `${msgEvent.eventtype}`
//          },
//          token: FCMToken
//      };

//     log('###### event-log: #######' + msgEvent);

// return admin.messaging().sendToTopic(topic, payload)
//     .then(function(response){
//          console.log('Notification sent successfully:',response);
//     }) 
//     .catch(function(error){
//          console.log('Notification sent failed:',error);
//     });


// });

exports.sendNotification  =  functions.database.ref('/events/{pushId}').onWrite(event => {
    const topic = "weatherEvents"
    const msgEvent= event.after.val();

    const payload = {notification: {
        title: `${msgEvent.eventtype}`,
        body: `Check out this new event!`
        }
    };

   log('###### event-log: #######' + msgEvent);

return admin.messaging().sendToTopic(topic, payload)
   .then(function(response){
        console.log('Notification sent successfully:',response);
   }) 
   .catch(function(error){
        console.log('Notification sent failed:',error);
   });


});
// sendHttpPushNotification 
//sendListenerPushNotification 


