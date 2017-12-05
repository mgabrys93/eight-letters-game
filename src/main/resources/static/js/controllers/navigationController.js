angular.module('chat.controllers').controller('NavigationController', function($rootScope, $http, $location, $route) {

    var self = this;

    self.tab = function(route) {
        return $route.current && route === $route.current.controller;
    };

    var authenticate = function(credentials, callback) {
        var headers = credentials ? {
            Authorization : "Basic "
            + btoa(credentials.username + ":"
                + credentials.password)
        } : {};

        $http.get('user', {
            headers : headers
        }).then(function(response) {
            if (response.data.name) {
                $rootScope.authenticated = true;
            } else {
                $rootScope.authenticated = false;
            }
            callback && callback($rootScope.authenticated);
        }, function() {
            $rootScope.authenticated = false;
            callback && callback(false);
        });

    };

    self.credentials = {};
    self.login = function() {
        authenticate(self.credentials, function(authenticated) {
            if (authenticated) {
                $location.path("/");
                self.error = false;
                $rootScope.authenticated = true;
            } else {
                $location.path("/login");
                self.error = true;
                $rootScope.authenticated = false;
            }
        })
    };

    self.logout = function() {
        $http.post('logout', {}).finally(function() {
            $rootScope.authenticated = false;
            $location.path("/");
        });
    }

});