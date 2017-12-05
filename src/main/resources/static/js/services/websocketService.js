angular.module('chat.services').service('WebsocketService', function ($rootScope) {
    var socket = {
        client: null,
        stomp: null,
        connected: false
    };

    var serviceProperties = {
        socketUrl: "/gs-guide-websocket",
        chatTopic: "/topic/message",
        chatBroker: "/app/message"
    };

    var initialize = function () {
        socket.client = new SockJS(serviceProperties.socketUrl);
        socket.stomp = Stomp.over(socket.client);

    };


    var disconnect = function () {
        socket.stomp.disconnect();
        socket.connected = false;
    };

    var setup = function () {
        if(socket.connected){
            disconnect();
        }
        initialize();
    };

    this.sendChat = function (message) {
        socket.stomp.send(serviceProperties.chatBroker, {}, JSON.stringify(message));
    };

    this.subscribeChat = function () {
        setup();
        socket.stomp.connect({}, function () {
            socket.connected = true;
            socket.stomp.subscribe(serviceProperties.chatTopic, function (data) {
                $rootScope.$broadcast('newMessageEvent', JSON.parse(data.body));
            });
        })
    };

    this.sendGameStatus = function (gameStatus, gameId) {
        socket.stomp.send("/app/game/" + gameId + "/status", {}, JSON.stringify(gameStatus));
    };

    this.subscribeGame = function (gameId) {
        setup();
        socket.stomp.connect({}, function () {
            socket.connected = true;
            socket.stomp.subscribe("/topic/game/" + gameId + "/status", function (data) {
                $rootScope.$broadcast('newGameStatusEvent', JSON.parse(data.body));
            });
            socket.stomp.subscribe("/topic/game/" + gameId + "/players", function (data) {
                $rootScope.$broadcast('newPlayerEvent', JSON.parse(data.body));
            });
        })
    };

});