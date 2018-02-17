
//global function counter:
global.schedule_data_demo_functionCounter = {


};

//
let start_time;
let checkRef;
let pathRef;
let path; //TODO: search
let schedule_timestamp;
let dataExists;



// dateFormat library
var dateFormat = require('dateformat');
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

// Reusable promise(callback) function to pass any mySql query and fetch response.
let fetchRecords = function(sql) {
  return new Promise (function(resolve, reject) {
    con.query(sql, function (err ,  result){
      if(err) reject(err);
      resolve(result);
    });
  });
};

fetchRecords("SELECT start_time, end_time, room_id, timestamp, Program, Batch, Course, Semester, Faculty, Division, id FROM mrbs_entry").then(function(resolved_args){

  /*passing the resolved arguments to another function for fetch the room number , which is called the
    setRoomName() function.
  */
  setRoomName(resolved_args);

}).catch(function(rejection_arg){
  console.error(rejection_arg);
});

//Set room_id to the room_name retrive and send it forward to firebase.
function setRoomName(value){
  let Resultset1P = value;
console.log(Resultset1P.length);
  for(let i = 0; i<Resultset1P.length; i++) {

    con.query("Select room_name , id from mrbs_room where id = '" + Resultset1P[i].room_id + "'", function (err, result_args){
      if (err) throw err;
      let j =0;
      while(j<result_args.length){
         roomName = result_args[j].room_name;
        if(roomName == null && Resultset1P[i] == null){
          // TODO add some code here.
        } else {
            Resultset1P[i].room_id = roomName;

            // call to saveRecord() function which stores the firebase database.
            saveRecord(Resultset1P[i]);
        }
        j++;
      }
    });
  }
}

function saveRecord(value){
  let Resultset1NP = [];
  Resultset1NP = value;

  // cal
  feedDatainFirebase(Resultset);
}
// "dddd, mmmm dS, yyyy, h:MM:ss TT"
function feedDatainFirebase(value){

  let Resultset2NP = value;
  let program;

  updated_start_time = dateFormat(new Date(Resultset2NP.start_time * 10000) , "h:MM:ss TT" );
  updated_end_time = dateFormat(new Date(Resultset2NP.end_time * 10000) , "h:MM:ss TT" );
  schedule_timestamp = dateFormat(new Date(Resultset2NP.start_time * 10000), "dddd, mmmm dS");
  // setting node path to store Resultset in respective nodes.
  //assign correct Batch to the variable.
  if(Resultset2NP.Batch == "Batch1518") {
    program = "1518";
  } else if (Resultset == "Batch1619") {
    program = "1619";
  } else if (Resultset2NP.Batch == "Batch1720") {
    program = "1720";
  } else {
    program = "";
  }
  //assigning correct program to the variable.
  if(Resultset2NP.Program == "bca") {
    program += "bca";
  }else if (Resultset2NP.Program == "bba-it") {
    program += "bba-it";
  } else if (Resultset2NP.Program == "mca") {
    program += "mca";
  } else if (Resultset2NP.Program == "mss") {
    program += "mss";
  } else if (Resultset2NP.Program == "mba-it") {
    program += "mba-it";
  } else {
    program += "";
  }

  path= Resultset2NP.Batch+"/"+program+"/"+Resultset2NP.Semester+"/Div"+Resultset2NP.Division+"/"+Resultset2NP.Course+"/"+schedule_timestamp;

  console.log(path);
  pathRef = firebase.database().ref(path+"/LectureID-"+Resultset2NP.id);
  checkRef = firebase.database().ref(path);
  checkRef.once("value").then(function(snapshot){
    var dataExists = snapshot.child("LectureID-"+Resultset2NP.id).exists();
    if(!dataExists) {
      pathRef.set ({
            course_name: Resultset.Course,
              batch_name : Resultset.Batch,
              division : Resultset.Division,
              sem : Resultset.Semester,
            program_name : Resultset.Program,
            room_number : Resultset.room_id,
            start_time : start_time,
            end_time : end_time,
            teacher_name : Resultset.Faculty,
             timestamp : Resultset.timestamp
          });
      } else {console.log("Data already exists");}
  });


  checkRef = firebase.database().ref("Lectures/"+schedule_timestamp);
  checkRef.once("value").then(function(snapshot){
    dataExists = snapshot.child("LectureID-"+Resultset.id).exists();
  if(!dataExists) {
    let trackRef =firebase.database().ref("/Lectures/"+schedule_timestamp+"/LectureID-"+Resultset.id);
    trackRef.set ({
          course_name: Resultset.Course,
            batch_name : Resultset.Batch,
            division : Resultset.Division,
            sem : Resultset.Semester,
          program_name : Resultset.Program,
          room_number : Resultset.room_id,
          start_time : start_time,
          end_time : end_time,
          teacher_name : Resultset.Faculty,
          timestamp : Resultset.timestamp
        });
    } else {console.log("Data already exists");}
  });

//function call filter out lectures: facultyFilter()
facultyFilter(Resultset2NP);
console.log(Resultset2NP);
}

function facultyFilter(value) {
  let Resultset2P = value;
   checkRef = "/User"+Resultset2P+"/Faculty";
   pathRef = "";

    if(Resultset2P != null) {
      // TODO:
    } else {
      //checking if FacultyCode exists in the firebase database.
      checkRef.once("value").then(function(snapshot) {
        dataExists = snapshot.child(Resultset2P.Faculty).exists();
        if(dataExists) {
          // no updation needed in database
          console.log('Faculty exists.');
          //TODO: not sure about break.

        } else {
          // adding the lecture at individual faculty code keyed node.
            // first checking if the lecture already exists. it already exsits.

          pathRef = "/Users/"+Resultset2P.Faculty+"/Lectures/"+schedule_timestamp+"/LectureID-"+Resultset2P.id; // path to set.
          checkRef = "/Users/"+Resultset2P.Faculty+"/Lectures/"+schedule_timestamp; // path for once check.
          //check if Lecture already exists.
          checkRef.once("value").then(function(snapshot) {
            dataExists = snapshot.child("/LectureID-"+Resultset2P.id).exists();
            if(dataExists) {

              //TODO : not sure about break functionality:

            } else {
              pathRef.set({
                    course_name: Resultset2P.Course,
                      batch_name : Resultset2P.Batch,
                      division : Resultset2P.Division,
                      sem : Resultset2P.Semester,
                    program_name : Resultset2P.Program,
                    room_number : Resultset2P.room_id,
                    start_time : start_time,
                    end_time : end_time,
                    teacher_name : Resultset2P.Faculty,
                    timestamp : Resultset2P.timestamp
                  });
                  console.error("done");
            }
          });
        }
      }
    }
  }
