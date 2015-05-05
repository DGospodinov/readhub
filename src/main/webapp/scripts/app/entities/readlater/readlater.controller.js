'use strict';

angular.module('readhubApp')
    .controller('ReadlaterController', function ($scope, Readlater, Item, User) {
        $scope.readlaters = [];
        $scope.items = Item.query();
        $scope.users = User.query();
        $scope.loadAll = function() {
            Readlater.query(function(result) {
               $scope.readlaters = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Readlater.update($scope.readlater,
                function () {
                    $scope.loadAll();
                    $('#saveReadlaterModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Readlater.get({id: id}, function(result) {
                $scope.readlater = result;
                $('#saveReadlaterModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Readlater.get({id: id}, function(result) {
                $scope.readlater = result;
                $('#deleteReadlaterConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Readlater.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteReadlaterConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.readlater = {id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
