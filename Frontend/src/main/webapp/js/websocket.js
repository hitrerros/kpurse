var stompClient = null;


function connect() {
    var socket = new SockJS('/endpoint');
    stompClient = Stomp.over(socket);

     stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/purse/processor', function (messageOutput) {
            process(JSON.parse(messageOutput.body));
        });
    });
   }


function process(messageOutput){
 alert("1");
}
