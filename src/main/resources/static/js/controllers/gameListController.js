angular.module('chat.controllers').controller('GameListController', function ($scope, $http, $location) {

    function getActiveGameList() {
       $http.get("/game/list/active").then(function (response) {
           $scope.gameList = response.data;
       }, function (error) {

       })
   }
   $scope.go = function (path) {
       $location.path("/game/" + path);
   }
   getActiveGameList();

});