
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

// Reusable promise(callback) function to pass any mySql query and fetch response. func-1
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

//Set room_id to the room_name retrive and send it forward to firebase. func-2
function setRoomName(value){
  let Resultset = value;

  for(let i = 0; i<Resultset.length; i++) {

    con.query("Select room_name , id from mrbs_room where id = '" + Resultset[i].room_id + "'", function (err, result_args){
      if (err) throw err;
      let j =0;
      while(j<result_args.length){
        roomName = result_args[j].room_name;
        if(roomName == null && Resultset1P[i] == null){
          // TODO add some code here.
        } else {
          Resultset[i].room_id = roomName;

          // call to feedDatainFirebase() function which stores the firebase database.
          feedDatainFirebase(Resultset[i]);
        }
        j++;
      }
    });
  }
}
// func-3
//function to save the records in firebase database at node:
// "dddd, mmmm dS, yyyy, h:MM:ss TT"
function feedDatainFirebase(value){
  let Resultset = [];
  Resultset = value;
  let program;
  let path;
  let path_to_fetch_courseName = "";
  let pathRef_to_fetch_courseName = "";
  let fetched_courseName = "";


  if(Resultset != null) {
    let updated_start_time = dateFormat(new Date(Resultset.start_time * 10000) , "h:MM:ss TT" );
    let updated_end_time = dateFormat(new Date(Resultset.end_time * 10000) , "h:MM:ss TT" );
    let schedule_timestamp = dateFormat(new Date(Resultset.start_time * 10000), "dddd, mmmm dS");
    // setting node path to store Resultset in respective nodes.
    //assign correct Batch to the variable.
    if(Resultset.Batch == "Batch1518") {
      program = "1518";
    } else if (Resultset.Batch == "Batch1619") {
      program = "1619";
    } else if (Resultset.Batch == "Batch1720") {
      program = "1720";
    } else {
      program = "";
    }
    //assigning correct program to the variable.
    if(Resultset.Program == "bca") {
      program += "bca";
    }else if (Resultset.Program == "bba-it") {
      program += "bba-it";
    } else if (Resultset.Program == "mca") {
      program += "mca";
    } else if (Resultset.Program == "mss") {
      program += "mss";
    } else if (Resultset.Program == "mba-it") {
      program += "mba-it";
    } else {
      program += "";
    }

    // path to where lecture info will be stored batch->program->semester->division->course->timestamp
    path= "/"+Resultset.Batch+"/"+program+"/"+Resultset.Semester+"/Div"+Resultset.Division+"/"+Resultset.Course+"/"+schedule_timestamp;
    path_to_fetch_courseName = "/"+Resultset.Batch+"/"+program+"/"+Resultset.Semester+"/Courses";
    // path_to_fetch_courseName : <batch>/<program>/<semester>/Courses.
    pathRef_to_fetch_courseName = firebase.database().ref( "/"+Resultset.Batch+"/"+program+"/"+Resultset.Semester+"/Courses");
    // fetching course name using course code : before inserting.
    pathRef_to_fetch_courseName.once("value").then(function(snapshot) {
      //this variable can be null, for it to be not null, select semester and course should match when making time-table.
      fetched_courseName = snapshot.child(Resultset.Course).val();

      //Store path at  batch->program->semester->division->course->timestamp->lectureid-> lecture_info
      let store1_pathRef = firebase.database().ref(path+"/LectureID-"+Resultset.id);
      // check path at batch->program->semester->division->course->timestamp
      let verify1_checkRef = firebase.database().ref(path);
      verify1_checkRef.once("value").then(function(snapshot){
        var dataExists = snapshot.child("LectureID-"+Resultset.id).exists();
        if(!dataExists) {
          store1_pathRef.set ({
            course_code: Resultset.Course,
            course_name : fetched_courseName,
            batch_name : Resultset.Batch,
            division : Resultset.Division,
            sem : Resultset.Semester,
            program_name : Resultset.Program,
            room_number : Resultset.room_id,
            start_time : updated_start_time,
            end_time : updated_end_time,
            teacher_name : Resultset.Faculty,
            timestamp : schedule_timestamp
          });
        } else { }
      }).catch(function(error) {
        console.error(error);
      });

      //check Ref: /Lectures/timestamp
      let verify2_checkRef = firebase.database().ref("Lectures/"+schedule_timestamp);
      verify2_checkRef.once("value").then(function(snapshot){
        dataExists = snapshot.child("LectureID-"+Resultset.id).exists();
        if(!dataExists) {

          //atore path : /Lectures/timestamp/LectureID-<id>
          let store2_pathRef =firebase.database().ref("/Lectures/"+schedule_timestamp+"/LectureID-"+Resultset.id);
          store2_pathRef.set ({
            course_code: Resultset.Course,
            course_name : fetched_courseName,
            batch_name : Resultset.Batch,
            division : Resultset.Division,
            sem : Resultset.Semester,
            program_name : Resultset.Program,
            room_number : Resultset.room_id,
            start_time : updated_start_time,
            end_time : updated_end_time,
            teacher_name : Resultset.Faculty,
            timestamp : schedule_timestamp
          });
        } else { }
      }).catch(function(error) {
        console.error(error);
      });
    }).catch(function(error) {
      console.error(error);
    });

    //function call filter out lectures: facultyFilter()
    facultyFilter(Resultset , path_to_fetch_courseName);

  } else {
    // TODO : Resultset is empty
  }

}

//func 4.
//Stores lecture info for each faculty
function facultyFilter(value , path) {
  let Resultset = value;
  // path_to_fetch_courseName : <batch>/<program>/<semester>/Courses.
  let path_to_fetch_courseName = path;
  let pathRef_to_fetch_courseName = "";
  let fetched_courseName = "";


  // func scope variables. updated time and date stamps
  let updated_start_time = dateFormat(new Date(Resultset.start_time * 10000) , "h:MM:ss TT" );
  let updated_end_time = dateFormat(new Date(Resultset.end_time * 10000) , "h:MM:ss TT" );
  let schedule_timestamp = dateFormat(new Date(Resultset.start_time * 10000), "dddd, mmmm dS");

  // checking if faculty code exists at /Users/Faculty
  let verify1_checkRef = firebase.database().ref("/Users/Faculty");

  // condition to check if arg passed is empty ot not. Resultset.
  if(Resultset == null) {

    // TODO: Resultset is empty or null

  } else {
    //checking if FacultyCode exists in the firebase database. /Users/Faculty
    verify1_checkRef.once("value").then(function(snapshot) {
      var key = snapshot.key;
      var dataExists = snapshot.child(Resultset.Faculty).exists();

      if(dataExists) {
        pathRef_to_fetch_courseName = firebase.database().ref(path_to_fetch_courseName);
      // fetching course name using course code : before inserting.
      pathRef_to_fetch_courseName.once("value").then(function(snapshot) {
        //this variable can be null, for it to be not null, select semester and course should match when making time-table.
          fetched_courseName = snapshot.child(Resultset.Course).val();

          // adding the lecture at individual faculty code keyed node.
          // it already exsits.

          let store1_pathRef = firebase.database().ref("/Users/"+Resultset.Faculty+"/Lectures/"+schedule_timestamp+"/LectureID-"+Resultset.id); // path to set.
          let verify2_checkRef = firebase.database().ref("/Users/"+Resultset.Faculty+"/Lectures/"+schedule_timestamp); // path for once check.
          //check if Lecture already exists.
          verify2_checkRef.once("value").then(function(snapshot) {

            var dataExists = snapshot.child("/LectureID-"+Resultset.id).exists();
            if(dataExists) {

              //TODO : not sure about break functionality:

            } else {
              store1_pathRef.set({
                course_code: Resultset.Course,
                course_name : fetched_courseName,
                batch_name : Resultset.Batch,
                division : Resultset.Division,
                sem : Resultset.Semester,
                program_name : Resultset.Program,
                room_number : Resultset.room_id,
                start_time : updated_start_time,
                end_time : updated_end_time,
                teacher_name : Resultset.Faculty,
                timestamp : schedule_timestamp
              });
            }
          }).catch(function(error) {
            console.error(error);
          });
      }).catch(function(error) {
        console.error(error);
      });
      } else {
        // The faculty doesnt exists
        console.log('Faculty doesnt exists.');
      }

    }).catch(function(error) {
      console.error(error);
    });
  }
}
