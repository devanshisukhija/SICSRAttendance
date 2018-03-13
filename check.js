var dateFormat = require('dateformat');
let updated_start_time = dateFormat(new Date(1520055000* 1000) , "h:MM:ss TT" );
console.log(updated_start_time);
