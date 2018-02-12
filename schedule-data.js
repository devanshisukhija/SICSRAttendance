let mysql,
    con,
    firebase,
    roomName;
global.SICSRglobalvariable = {
  Resultset : null
};

// Connection to MySql sever.
mysql = require('mysql');
con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "root",
  database: "mrbs"
});

// Connection to Firebase.
firebase = require('firebase');
firebase.initializeApp({
  serviceAccount: "./SICSR-d924e501f52d.json",
  databaseURL: "https://sicsr-d4771.firebaseio.com"
});

// Connection check to MySql Server.
con.connect(function(err) {
  if (err) throw err;
  console.log("connected");
});
// , FROM_UNIXTIME(end_time,'%h:%i:%s'), timestamp
let fetchRecords = function(sql) {
  return new Promise (function(resolve, reject) {
    con.query(sql, function (err ,  result){
      if(err) reject(err);
      resolve(result);
    });
  });
};

// Fetching data from SQL Sever.

fetchRecords("SELECT start_time  FROM mrbs_entry ").then(function(fromResolve){

    let Resultset = fromResolve;
    for(let i = 0; i<Resultset.length; i++) {
let startTime = Resultset[i].start_time;
      console.log("1 done" + startTime);


      fetchRecords("SELECT FROM_UNIXTIME(("+startTime+"),  '%h:%m:%s' )").then(function(fromResolve){
          let sTime = fromResolve;
          console.log("2 done" + sTime);


          fetchRecords("Update mrbs_entry set start_time = '"+sTime+"'").then(function(fromResolve){
            console.log("3 done" + sql);
          }).catch(function(fromReject){
            console.log("3 fail"+ fromReject);
          });

      }).catch(function(fromReject){
        console.log("2 fail"+fromReject);
      });
    }
}).catch(function(fromReject){
console.log("1 fail"+fromReject);
});

// con.query("SELECT start_time  FROM mrbs_entry ", function (err , result){
//   if(err) throw err;
//     let Resultset = result;
// for(let i = 0; i<Resultset.length; i++) {
//     let startTime = Resultset[i].start_time;
//     var timeStamp = Resultset[i].timestamp;
//
//     con.query("SELECT FROM_UNIXTIME("+startTime+")", function)
//       console.log();
//
// }
    //getRoomName(result);
     // SICSRglobalvariable.Resultset = result;
    // let j=0;
    // for(let i = 0; i<SICSRglobalvariable.Resultset.length; i++) {
    //
    //   con.query("Select room_name , id from mrbs_room where id = '" +SICSRglobalvariable.Resultset[i].room_id+ "'", function (err, result2){
    //     if (err) throw err;


        // while(j<result2.length){
        //
        //    roomName = result2[j].room_name;
        //
        //   if(roomName == null && SICSRglobalvariable.Resultset[i] == null){
        //     // TODO add some code here.
        //   } else {
        //
        //     setRoomName(roomName, SICSRglobalvariable.Resultset[i]);
        //   }
        //
        //   j++;
        // }

  //}

  // Set the value of room_id. (From orignal value of room_id to the value of room_name).
  //And calling firebase function to insert the value of final Resultset into firebase DB.
//   function setRoomName(roomName, object) {
//     let localResultset = object;
//     if(localResultset == null &&  roomName == null){
//       console.log("room_id in Resultset is not updated to room_name ");
//     } else {
//       localResultset.room_id = roomName;
//       //console.log(global);
//     //  updateFirebaseDB(localResultset);
//     }
//   }
// });
//
// // fetch room_name against each room id and forwarding it to setRoomName.
// function getRoomName(value){
//   var objValue = value;
//    SICSRglobalvariable.Resultset = objValue;
//    let j =0;
//
//
//   for(let i = 0; i<SICSRglobalvariable.Resultset.length; i++) {
//
//     con.query("Select room_name , id from mrbs_room where id = '" +SICSRglobalvariable.Resultset[i].room_id+ "'", function (err, result2){
//       if (err) throw err;
//
//
//       while(j<result2.length){
//
//          roomName = result2[j].room_name;
//
//         if(roomName == null && SICSRglobalvariable.Resultset[i] == null){
//           // TODO add some code here.
//         } else {
//
//           setRoomName(roomName, SICSRglobalvariable.Resultset[i]);
//         }
//
//         j++;
//       }
//     });
//   }
// }
//
// // Set the value of room_id. (From orignal value of room_id to the value of room_name).
// //And calling firebase function to insert the value of final Resultset into firebase DB.
// function setRoomName(roomName, object) {
//   let localResultset = object;
//   if(localResultset == null &&  roomName == null){
//     console.log("room_id in Resultset is not updated to room_name ");
//   } else {
//     localResultset.room_id = roomName;
//     //console.log(global);
//   //  updateFirebaseDB(localResultset);
//   }
// }
// console.log(global);
// //console.log(global);
// //Firebase code.
// // function updateFirebaseDB(object) {
// //   var localResultset = object;
// //   let ref = firebase.database().ref("Lecture");
// //   let i=0;
// //   while(i<localResultset.length){
// //     ref.push({
// //       devanshi : "sukhija"
// //       // course_name: Resultset[i].Course,
// //       // program_name : Resultset[i].Program,
// //       // room_number : Resultset[i].room_id,
// //       // start_time : Resultset[i].start_time,
// //       // end_time : Resultset[i].end_time,
// //       // teacher_name : Resultset[i].Faculty,
// //       // timestamp : Resultset[i].timestamp
// //
// //     });
// //
// //     i++;
// //   }
// // }
//
// // (function firebase_update(){
// //   let ref = firebase.database().ref("Lecture");
// //   console.log(global);
// //   ref.push({
// //     devanshi : "avienaya"
// //   });
// // })();
