'use strict';

angular.module('readhubApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
