
// dateFormat library
var dateFormat = require('dateformat');
let global_array = {};

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
  setRoomName(resolved_args)
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
        if(roomName == null && Resultset[i] == null){
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
    let updated_start_time = dateFormat(new Date(Resultset.start_time * 1000) , "hh:MM" );
    let updated_end_time = dateFormat(new Date(Resultset.end_time * 1000) , "hh:MM" );
    let schedule_timestamp = dateFormat(new Date(Resultset.start_time * 1000), "dddd, mmmm dd");
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
    // path 0 to where lecture info will be stored batch->program->semester->division->course->timestamp
    path= "/"+Resultset.Batch+"/"+program+"/"+Resultset.Semester+"/Div"+Resultset.Division+"/LecturesCourseWise/"+Resultset.Course+"/"+schedule_timestamp;
    //path 1 to where lectures will be stored batch-> program->semester->Division->Lectures
    path1 = "/"+Resultset.Batch+"/"+program+"/"+Resultset.Semester+"/Div"+Resultset.Division;
    path_to_fetch_courseName = "/"+Resultset.Batch+"/"+program+"/"+Resultset.Semester+"/Courses";
    // path_to_fetch_courseName : <batch>/<program>/<semester>/Courses.
    pathRef_to_fetch_courseName = firebase.database().ref( "/"+Resultset.Batch+"/"+program+"/"+Resultset.Semester+"/Courses");
    // fetching course name using course code : before inserting.
    pathRef_to_fetch_courseName.once("value").then(function(snapshot) {
      //this variable can be null, for it to be not null, select semester and course should match when making time-table.
      fetched_courseName = snapshot.child(Resultset.Course).val();
      //Path ref at  batch->program->semester->division->course->timestamp
      let store1_pathRef = firebase.database().ref(path);
      //check path at Lectures
      let verify1_checkRef = firebase.database().ref("/Lectures");
      let store3_pathRef = firebase.database().ref("Lectures/"+schedule_timestamp+"/Lecture-ID-"+Resultset.id);
      verify1_checkRef.once("value").then(function(snapshot){
            if(!snapshot.child("Lecture-ID"+Resultset.lectureId).exists()){
              store3_pathRef.push({
                course_code: Resultset.Course,
                course_name : fetched_courseName,
                batch_name : Resultset.Batch,
                division : Resultset.Division,
                sem : Resultset.Semester,
                program_name : Resultset.Program,
                room_number : Resultset.room_id,
                start_time : Resultset.start_time,
                modified_start_time : updated_start_time,
                modified_end_time : updated_end_time,
                end_time : Resultset.end_time,
                teacher_name : Resultset.Faculty,
                timestamp : schedule_timestamp,
                lectureId : Resultset.id
              });
            } else {  console.log(" no")}


    // path 0 to where lecture info will be stored batch->program->semester->division->LecturesCourseWise->course->timestamp
     let verify3_checkRef = firebase.database().ref("/"+Resultset.Batch+"/"+program+"/"+Resultset.Semester+"/Div"+Resultset.Division+"/LecturesCourseWise");
     verify3_checkRef.once("value").then(function(snapshot){
     if(snapshot.child(Resultset.Course+"/"+schedule_timestamp)){
        store1_pathRef.push({
          course_code: Resultset.Course,
          course_name : fetched_courseName,
          batch_name : Resultset.Batch,
          division : Resultset.Division,
          sem : Resultset.Semester,
          program_name : Resultset.Program,
          room_number : Resultset.room_id,
          start_time : Resultset.start_time,
          modified_start_time : updated_start_time,
          modified_end_time : updated_end_time,
          end_time : Resultset.end_time,
          teacher_name : Resultset.Faculty,
          timestamp : schedule_timestamp,
          lectureId : Resultset.id
      });
     } else {
      snapshot.forEach(function(child) {
        child.forEach(function(innerchild){
        if(innerchild.child("lectureId").val() == Resultset.lectureId){
            console.log("Exists: got lectureId")
          } else {
              store1_pathRef.push({
          course_code: Resultset.Course,
          course_name : fetched_courseName,
          batch_name : Resultset.Batch,
          division : Resultset.Division,
          sem : Resultset.Semester,
          program_name : Resultset.Program,
          room_number : Resultset.room_id,
          start_time : Resultset.start_time,
          modified_start_time : updated_start_time,
          modified_end_time : updated_end_time,
          end_time : Resultset.end_time,
          teacher_name : Resultset.Faculty,
          timestamp : schedule_timestamp,
          lectureId : Resultset.id
              });
          }
          });
        });
        }
     }).catch(function(error) {
     console.error(error);
     })
}).catch(function(error) {
     console.error(error);
     })

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
  let uid;
  let faculty_uidRef;


  // func scope variables. updated time and date stamps
  let updated_start_time = dateFormat(new Date(Resultset.start_time * 1000) , "hh:MM" );
  let updated_end_time = dateFormat(new Date(Resultset.end_time * 1000) , "hh:MM" );
  let schedule_timestamp = dateFormat(new Date(Resultset.start_time * 1000), "dddd, mmmm dd");

  // checking if faculty code exists at /Users/Faculty
  let path_to_getUID = firebase.database().ref("/Users/Faculty");



  // condition to check if arg passed is empty ot not. Resultset.
  if(Resultset == null) {
    // TODO: Resultset is empty or null
  } else {
    //checking if FacultyCode exists in the firebase database. /Users/Faculty
    path_to_getUID.once("value").then(function(snapshot) {
      var dataExists = snapshot.child(Resultset.Faculty).exists();
      if(dataExists) {
          uid = snapshot.child(Resultset.Faculty).val();
        pathRef_to_fetch_courseName = firebase.database().ref(path_to_fetch_courseName);
      // fetching course name using course code : before inserting.
      pathRef_to_fetch_courseName.once("value").then(function(snapshot) {
        //this variable can be null, for it to be not null, select semester and course should match when making time-table.
          fetched_courseName = snapshot.child(Resultset.Course).val();

          // adding the lecture at individual faculty code keyed node.
          // it already exsits.
          console.log(uid)
          faculty_uidRef = "/Users/"+uid+"/Lectures/"+schedule_timestamp;
          let store5_pathRef = firebase.database().ref(faculty_uidRef); // path to set.
          let verify2_checkRef = firebase.database().ref("/Users/"+uid+"/Lectures/"); // path for on check.
          //check if Lecture already exists.
          verify2_checkRef.once("value").then(function(snapshot) {
          if(!snapshot.child(schedule_timestamp).exists()){
            store5_pathRef.push({
            course_code: Resultset.Course,
            course_name : fetched_courseName,
            batch_name : Resultset.Batch,
            division : Resultset.Division,
            sem : Resultset.Semester,
            program_name : Resultset.Program,
            room_number : Resultset.room_id,
            start_time : Resultset.start_time,
            modified_start_time : updated_start_time,
            modified_end_time : updated_end_time,
            end_time : Resultset.end_time,
            teacher_name : Resultset.Faculty,
            timestamp : schedule_timestamp,
            lectureId : Resultset.id
          });
          } else {
            snapshot.forEach(function(child) {
             child.forEach(function(innerchild){
             if(innerchild.child("lectureId").val() == Resultset.lectureId){
                console.log("got the lecture ID")
              } else {
              store5_pathRef.push({
                course_code: Resultset.Course,
                course_name : fetched_courseName,
                batch_name : Resultset.Batch,
                division : Resultset.Division,
                sem : Resultset.Semester,
                program_name : Resultset.Program,
                room_number : Resultset.room_id,
                start_time : Resultset.start_time,
                modified_start_time : updated_start_time,
                modified_end_time : updated_end_time,
                end_time : Resultset.end_time,
                teacher_name : Resultset.Faculty,
                timestamp : schedule_timestamp,
                lectureId : Resultset.id
              });
            }
            });
            });
            }
          }).catch(function(error) {
            console.error(error);
          });

          var faculty_uidRefCourse = "/Users/"+uid+"/LecturesCourseWise/"+Resultset.Course+"/"+schedule_timestamp;
          var verify4_checkRef = firebase.database().ref("/Users/"+uid+"/LecturesCourseWise/")
          var store4_pathRef = firebase.database().ref(faculty_uidRefCourse)
          verify4_checkRef.once("value").then(function(snapshot) {
          if(!snapshot.child(Resultset.Course+"/"+schedule_timestamp).exists()){
                    store4_pathRef.push({
                      course_code: Resultset.Course,
                      course_name : fetched_courseName,
                      batch_name : Resultset.Batch,
                      division : Resultset.Division,
                      sem : Resultset.Semester,
                      program_name : Resultset.Program,
                      room_number : Resultset.room_id,
                      start_time : Resultset.start_time,
                      modified_start_time : updated_start_time,
                      modified_end_time : updated_end_time,
                      end_time : Resultset.end_time,
                      teacher_name : Resultset.Faculty,
                      timestamp : schedule_timestamp,
                      lectureId : Resultset.id
                    });

                  } else {
                  snapshot.forEach(function(child) {
                   child.forEach(function(innerchild){
                    if(innerchild.child("lectureId").val() == Resultset.lectureId){
                      console.log("got the lecture ID")
                    } else {
                    store4_pathRef.push({
                      course_code: Resultset.Course,
                      course_name : fetched_courseName,
                      batch_name : Resultset.Batch,
                      division : Resultset.Division,
                      sem : Resultset.Semester,
                      program_name : Resultset.Program,
                      room_number : Resultset.room_id,
                      start_time : Resultset.start_time,
                      modified_start_time : updated_start_time,
                      modified_end_time : updated_end_time,
                      end_time : Resultset.end_time,
                      teacher_name : Resultset.Faculty,
                      timestamp : schedule_timestamp,
                      lectureId : Resultset.id
                    });
                    }
                   });
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
