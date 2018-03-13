
// dateFormat library
var dateFormat = require('dateformat');
const util = require('util')

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


var lecture = firebase.database().ref("Lectures")
var divlec = firebase.database().ref("/Batch1518/1518bca/Sem I/DivA")
var sachin = firebase.database().ref("/Users/yZQBdIdF00Z3sGIZf4F4ds6Qx6N2/Lectures")
lecture.remove();
divlec.remove();
sachin.remove();

