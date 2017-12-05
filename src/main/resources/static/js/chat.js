angular.module('chat', ['ngMessages', 'ngRoute', 'chat.controllers', 'chat.services'])
    .config(function ($routeProvider, $httpProvider) {
        $routeProvider.when('/chat', {
            templateUrl: 'chat.html',
            controller: 'ChatController',
            controllerAs: 'chatCtrl'
        }).when('/login', {
            templateUrl: 'login.html',
            controller: 'NavigationController',
            controllerAs: 'navCtrl'
        }).when('/game/new', {
            templateUrl: 'newGame.html',
            controller: 'NewGameController',
            controllerAs: 'newGameCtrl'
        }).when('/game/:id', {
            templateUrl: 'gameView.html',
            controller: 'ViewGameController',
            controllerAs: 'viewGameCtrl'
        }).when('/game/list/active', {
            templateUrl: 'gameList.html',
            controller: 'GameListController',
            controllerAs: 'gameListCtrl'
        }).otherwise('/');

        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
    });
