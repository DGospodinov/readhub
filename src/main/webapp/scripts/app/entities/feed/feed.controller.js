'use strict';

angular.module('readhubApp')
    .controller('FeedController', function ($scope, Feed, User, Item, Category) {
        $scope.feeds = [];
        $scope.users = User.query();
        $scope.items = Item.query();
        $scope.categorys = Category.query();
        $scope.loadAll = function() {
            Feed.query(function(result) {
               $scope.feeds = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Feed.update($scope.feed,
                function () {
                    $scope.loadAll();
                    $('#saveFeedModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Feed.get({id: id}, function(result) {
                $scope.feed = result;
                $('#saveFeedModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Feed.get({id: id}, function(result) {
                $scope.feed = result;
                $('#deleteFeedConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Feed.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteFeedConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.feed = {url: null, name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
