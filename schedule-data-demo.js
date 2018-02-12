// Connection to MySql sever.
 const mysql = require('mysql');
con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "root",
  database: "mrbs"
});

// Connection to Firebase.
firebase = require('firebase');
firebase.initializeApp({
  serviceAccount: "./SICSR-d924xze501f52d.json",
  databaseURL: "https://sicsr-d4771.firebaseio.com"
});


let fetchRecords = function(sql) {
  return new Promise (function(resolve, reject) {
    con.query(sql, function (err ,  result){
      if(err) reject(err);
      resolve(result);
    });
  });
};

fetchRecords("SELECT start_time, end_time, room_id, timestamp, Program, Batch, Course, Semester, Faculty, Division, id FROM mrbs_entry").then(function(fromResolve){
  Resultset = fromResolve;
  setRoomName(Resultset);
  //console.log(global);
}).catch(function(fromReject){
  console.log(fromReject);
});

//set room_id to the room_name retrive and send it forward to firebase.
function setRoomName(value){
  let objValue = value;

  for(let i = 0; i<objValue.length; i++) {

    con.query("Select room_name , id from mrbs_room where id = '" + objValue[i].room_id + "'", function (err, result2){
      if (err) throw err;
      let j =0;
      while(j<result2.length){
         roomName = result2[j].room_name;
        if(roomName == null && objValue[i] == null){
          // TODO add some code here.
        } else {
            objValue[i].room_id = roomName;
            saveRecord(objValue[i]);
        }
        j++;
      }
    });
  }
}

function saveRecord(data){
  let Resultset = [];
  Resultset = data;
  feedDatainFirebase(Resultset);
}

function feedDatainFirebase(value){

  var Resultset = value;
   let ref = firebase.database().ref("Lecture/"+Resultset.timestamp);
    ref.once("value").then(function(snapshot){
     var dataExists = snapshot.child(Resultset.id).exists();
     if(!dataExists){
       let ref2 = firebase.database().ref("Lectures/"+Resultset.id);
       ref2.push({
         course_name: Resultset.Course,
           batch_name : Resultset.Batch,
           sem : Resultset.Semester,
         program_name : Resultset.Program,
         room_number : Resultset.room_id,
         start_time : Resultset.start_time,
         end_time : Resultset.end_time,
         teacher_name : Resultset.Faculty,
         timestamp : Resultset.timestamp
       });
     } else {console.log("Data already exists");}
   });
}
