'use strict';

angular.module('readhubApp')
    .controller('CategoryDetailController', function ($scope, $stateParams, Category, User, Feed,Categoryfeeds,CategoryItems) {
        $scope.categorys = [];
        $scope.loadAll = function() {
            Category.query(function(result) {
                $scope.categorys = result;
            });
        };

        $scope.category = [];
        $scope.load = function (id) {
            Category.get({id: id}, function(result) {
              $scope.category = result;
            });
        };
        $scope.load($stateParams.id);

        $scope.create = function () {
            Category.update($scope.category,
                function () {
                    $scope.loadAll();
                    $('#saveModal').modal('hide');
                    $scope.clear();
                });
        };
        $scope.update = function (id) {
            Category.get({id: id}, function(result) {
                $scope.category = result;
                $('#saveModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Category.get({id: id}, function(result) {
                $scope.category = result;
                $('#deleteConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Category.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteConfirmation').modal('hide');
                    $scope.clear();
                    $scope.category = [];
                    $scope.listItems = [];
                });
        };

        $scope.clear = function () {
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };

        $scope.listItems = [];
        $scope.loadItems = function(id){
            CategoryItems.get({id:id},function(result){
                $scope.listItems = result;
            })
        }
        $scope.loadItems($stateParams.id);

        $scope.layout = 'list';

        $scope.fullscreen =function(){
            angular.element('#wrapper').toggleClass("toggled");
        };
    });
