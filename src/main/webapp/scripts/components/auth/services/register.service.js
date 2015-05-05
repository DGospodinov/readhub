'use strict';

angular.module('readhubApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


