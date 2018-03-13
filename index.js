var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);



//function handler(req, res){
//  fs.readFile(__dirname + '/client/ioclient.html',
//    function(err, data) {
//      if(err){
//        res.writeHead(500);
//        return res.end('Error loading ioclient.html');
//      }
//      res.writeHead(200);
//      res.end(data);
//    });
//}

//Socket.io code; more than likely websocket protocol will be used at this point, if not, long polling will kick in
io.on('connection', function(socket){
//
//  socket.emit('event1', "event1 balle");//Send object to ioclient.html
//
//  socket.on('event2', function (data){
//    console.log(data);                    //log data received from ioclient.html
//  });

console.log("connection done " + socket.id);
});

http.listen(4555, function(){
console.log("Server is listening on port 4555");
})