'use strict';

angular.module('readhubApp')
    .controller('ReadlaterDetailController', function ($scope, $stateParams, Readlater, Item, User) {
        $scope.readlater = {};
        $scope.load = function (id) {
            Readlater.get({id: id}, function(result) {
              $scope.readlater = result;
            });
        };
        $scope.load($stateParams.id);
    });
