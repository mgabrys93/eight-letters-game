angular.module('chat.controllers').controller('ChatController', function ($scope, $http, WebsocketService) {
    $scope.messages = [];
    $scope.message = "";
    $scope.username = "";

    var messageObject = {
        message: "",
        authorUsername: "",
        messageDateTime: ""
    }
    $scope.addMessage = function () {
        messageObject.message = $scope.message;
        messageObject.authorUsername = $scope.username;
        WebsocketService.sendChat(messageObject);
        $scope.message = "";
    };

    function getCurrentUser() {
        $http.get('/user/current').then(function (response) {
           $scope.username = response.data;
        }, function (error) {

        });
    }

    function setup() {
        WebsocketService.subscribeChat();
        getCurrentUser();
    }
    setup();

    $scope.$on('newMessageEvent', function (event, data) {
        $scope.messages.unshift(data);
        $scope.$apply();
    });

});