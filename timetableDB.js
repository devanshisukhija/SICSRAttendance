// Connection to MySql sever.
var mysql = require('mysql');
var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "root",
  database: "mrbs"
});

// Connection to Firebase
var firebase = require('firebase');
firebase.initializeApp({
  serviceAccount: "./SICSR-d924e501f52d.json",
  databaseURL: "https://sicsr-d4771.firebaseio.com"
});

// Fetching data from SQL Sever.
con.connect(function(err) {
  if (err) throw err;
  console.log("connected");
});
con.query("SELECT start_time, end_time, room_id, timestamp, Program, Batch, Course, Semester, Faculty, Division FROM mrbs_entry", function (err , result) {
    if (err) throw err;
    var numRow = result.length;

    for(var i=0; i< 5; i++){

    var resultRoomId = result[i].room_id;
    var resultCourseName = result[i].Course;
    var resultStartTime = result[i].start_time;
    var resultEndTime = result[i].end_time;
    var resultTimeStamp = result[i].timestamp;
    var resultProgramName = result[i].Program;
    var resultTeacherName = result[i].Faulty;
    var resultSemester = result[i].Semester;
    var resultDivision = result[i].Division;


    con.query("Select room_name from mrbs_room where id = '" +resultRoomId + "'", function (err, result2){
     if (err) throw err;
      var result2RoomName = result2;
      });

    var ref = firebase.database().ref("Lecture");
      ref.push({
        course_name: resultCourseName,
        program_name : resultProgramName,
        room_number : result2RoomName,
        start_time : resultStartTime,
        end_time : resultEndTime,
        teacher_name : resultTeacherName
      });
