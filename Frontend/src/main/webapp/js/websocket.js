var stompClient = null;


function connect() {
    var socket = new SockJS('/purse/endpoint');
    stompClient = Stomp.over(socket);

     stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Socket connected: ' + frame);
        stompClient.subscribe('/purse/processor', function (messageOutput) {
            serverCallback(JSON.parse(messageOutput.body));
        });
    });
   }



function setConnected(connected) {

}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Socket disconnected");
}


function serverCallback(messageInput){

}


function sendToServer(messageOutput){
     stompClient.send("/app/main",{},JSON.stringify({'name':'test'}));
}


function income() {



    alert("1");
}


function withdrawal() {
    
    
}

$(function () {
    // $("form").on('submit', function (e) {
    //     e.preventDefault();
    // });

    $(document).on("click","[data-toggle='modal']", function ( ) {
        var trigger = $(this);
        var modal = $(trigger.data("target"));

        modal.one("hidden.bs.modal",function () {
            trigger.focus();
        })


    });


});