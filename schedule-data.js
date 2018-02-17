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
// Fetching records from database with callback functions.
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
  let program;
  let path;
  // setting node path to store Resultset in respective nodes.
  //assign correct Batch to the variable.
  if(Resultset.Batch == "Batch1518") {
    program = "1518";
  } else if (Resultset == "Batch1619") {
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
  // assigning path value to path variable
  path= Resultset.Batch+"/"+program+"/"+Resultset.Semester+"/Div"+Resultset.Division+"/"+Resultset.Course;
  //checking if data exsits , if not then uploading data to appropirate node.
  let pathRef = firebase.database().ref(path+"/LectureID-"+Resultset.id);
  var checkRef = firebase.database().ref(path);
  checkRef.once("value").then(function(snapshot){
    var dataExists = snapshot.child("LectureID-"+Resultset.id).exists();
    if(!dataExists) {
      pathRef.set ({
            course_name: Resultset.Course,
              batch_name : Resultset.Batch,
              division : Resultset.Division,
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
// adding lectures data to node: Lecture
  var checkRef = firebase.database().ref("Lectures");
  checkRef.once("value").then(function(snapshot){
  var dataExists = snapshot.child("LectureID-"+Resultset.id).exists();
  if(!dataExists) {
    let trackRef =firebase.database().ref("Lectures/LectureID-"+Resultset.id);
    trackRef.set ({
          course_name: Resultset.Course,
            batch_name : Resultset.Batch,
            division : Resultset.Division,
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
