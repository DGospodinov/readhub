'use strict';

angular.module('readhubApp')
    .controller('FeedDetailController', function ($scope, $stateParams,$state, Feed, User, FeedItems,Category) {
        $scope.items = [];
        $scope.load = function (id) {
            FeedItems.get({id: id}, function(result) {
                $scope.items = result;
            });
        };
        $scope.load($stateParams.id);

        $scope.feed = [];
        $scope.loadd = function(id){
            Feed.get({id:id},function(result){
                $scope.feed = result;
            });
        };
        $scope.loadd($stateParams.id);

        //Feed
        $scope.feeds = [];
        $scope.loadAll = function() {
            Feed.query(function(result) {
                $scope.feeds = result;
            });
        };
        $scope.create = function () {
            Feed.update($scope.feed,
                function () {
                    $scope.loadAll();
                    $('#saveModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Feed.get({id: id}, function(result) {
                $scope.feed = result;
                $('#saveModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Feed.get({id:id}, function(result) {
                $scope.feed = result;
                $('#deleteConfirmation').modal('show');
            });
        };


        $scope.confirmDelete = function (id) {
            Feed.delete({id: id},
                function () {
                    $('#deleteConfirmation').modal('hide');
                    $scope.clear();
                });
        };
        $scope.clear = function () {
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
        $scope.categorys = [];
        $scope.loadAllCat = function() {
            Category.query(function(result) {
                $scope.categorys = result;
            });
        };
        $scope.loadAllCat();
        $scope.layout = 'list';

        $scope.fullscreen =function(){
            angular.element('#wrapper').toggleClass("toggled");
        };
    });
