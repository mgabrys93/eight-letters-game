angular.module('chat.controllers').controller('NewGameController', function ($http, $scope, $location) {
    $scope.name = "";
    $scope.maxPlayers = 2;
    $scope.gameTime = 120;

    this.createNewGame = function () {
        $scope.submitted = true;
        var game = {
            name: $scope.name,
            maxPlayers: $scope.maxPlayers,
            gameTime: $scope.gameTime
        };
        if($scope.gameForm.$valid){
            $http.post('game/new/', game).then(function (response) {
                $scope.submitted = false;
                $location.path('game/' + response.data.id);
            }, function (error) {
            })
        }

    }
});