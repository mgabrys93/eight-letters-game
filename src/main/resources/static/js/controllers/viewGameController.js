angular.module('chat.controllers').controller('ViewGameController',
    function ($rootScope, $scope, $http, $location, $routeParams, $interval, WebsocketService) {
        $scope.word = "";
        $scope.username = "";
        $scope.errorMessage = "";
        $scope.words = [];
        $scope.players = {};
        $scope.playersNames = [];
        $scope.isPlayersConnected = false;
        $scope.isGameFinished = false;
        $scope.winnerName = "";
        $scope.gameStarted = false;


        var id = $routeParams.id;
        var gameStatus = {
            username: "",
            word: "",
            playersPoints: {}
        };

        function updatePlayersNames(playerNames) {
            $scope.players = {};
            $scope.playersNames = [];
            for(var i=0; i<playerNames.length; i++){
                $scope.players[playerNames[i]] = 0;
                $scope.playersNames.push(playerNames[i]);
            }
        }

        function setWinnerName() {
            $scope.isGameFinished = true;
            $scope.winnerName = Object.keys($scope.players).reduce(function(a, b){ return $scope.players[a] > $scope.players[b] ? a : b });
        }

        function changeGameVisibleCondition() {
            if(!$scope.isPlayersConnected){
                $scope.isPlayersConnected = !$scope.isPlayersConnected;
                startTimer();
            }
        }

        function checkIfArrayContainsWord(word) {
            return $scope.words.indexOf(word) > -1;
        }

        function checkIfPlayersConnected() {
            if($scope.maxPlayers === $scope.playersNames.length) changeGameVisibleCondition();
        }

        function updatePlayer(gameStatus) {
            $scope.players[gameStatus.username] = gameStatus.playersPoints[gameStatus.username];
        }

        function updateErrorMessage(errorMessage) {
            $scope.errorMessage = errorMessage;
        }

        $scope.checkWord = function () {
            if(checkIfArrayContainsWord($scope.word)) {
                updateErrorMessage("YOU CANNOT SEND SAME WORD TWICE!")
            }else {
                gameStatus.word = $scope.word;
                gameStatus.playersPoints = $scope.players;
                gameStatus.username = $scope.username;
                WebsocketService.sendGameStatus(gameStatus, id);
            }
            $scope.word = "";
        };

        $scope.$on('newGameStatusEvent', function (event, data) {
            switch (data.gameStatusEnum){
                case "SUCCESS":
                    updatePlayer(data);
                    if($scope.username !== data.username) break;
                    $scope.words.push(data.word);
                    updateErrorMessage("");
                    break;
                case "INVALID_LETTER":
                    if($scope.username === data.username) updateErrorMessage("INVALID LETTER");
                    break;
                case "INVALID_WORD":
                    if($scope.username === data.username) updateErrorMessage("INVALID WORD");
                    break;
            }
        });

        $scope.$on('newPlayerEvent', function (event, data) {
            updatePlayersNames(data);
            checkIfPlayersConnected();
        });

        function updateGameInfoFields(gameInfo) {
            $scope.gameName = gameInfo.name;
            $scope.maxPlayers = gameInfo.maxPlayers;
            $scope.gameTime = gameInfo.gameTime;
            $scope.counter = $scope.gameTime;
            $scope.characterSet = gameInfo.characterSet.split('');
        }

        function getGameInfo() {
            $http.get('/game/' + id).then(function (response) {
                updateGameInfoFields(response.data);
                updatePlayersNames(response.data.users);
                checkIfPlayersConnected();
            }, function (error) {
                $scope.gameStarted = true;
            });
        }

        function getCurrentUser() {
            $http.get('/user/current').then(function (response) {
                $scope.username = response.data;
            }, function (error) {
            });
        }

        var prepareGame = function() {
            WebsocketService.subscribeGame(id);
            getGameInfo();
            getCurrentUser();

        };

        var decreaseCounter = function () {
            if($scope.counter > 0) $scope.counter--;
            if($scope.counter === 0) stopTimer();
        };

        var promise;
        function stopTimer() {
            $interval.cancel(promise);
            setWinnerName();
        }

        function startTimer() {
            promise = $interval(decreaseCounter, 1000);
        }
        prepareGame();
});