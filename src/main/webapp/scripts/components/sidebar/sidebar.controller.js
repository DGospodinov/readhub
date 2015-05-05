'use strict';

angular.module('readhubApp')
    .controller('SidebarController', function ($scope,$stateParams, $location, $state,
                                               Auth, Principal,Feed ,Category,Categoryfeeds) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.feeds = Feed.query();
        $scope.categorys = [];

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
        //Feeds
        $scope.createFeed = function () {
            Feed.update($scope.feed,
                function () {
                    $scope.loadAllFeeds();
                    $('#saveFeedModal').modal('hide');
                    $scope.clearFeed();
                });
        };
        $scope.clearFeed = function () {
            $scope.feed = {url: null, name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
        $scope.loadAllFeeds = function() {
            Feed.query(function(result) {
                $scope.feeds = result;
            });
        };
        $scope.loadAllFeeds();

    //    CAtegory
        $scope.loadAllCat = function() {
            Category.query(function(result) {
                $scope.categorys = result;
            });
        };
        $scope.loadAllCat();

        $scope.createCat = function () {
            Category.update($scope.category,
                function () {
                    $scope.loadAllCat();
                    $('#saveCategoryModal').modal('hide');
                    $scope.clearCat();
                });
        };
        $scope.clearCat = function () {
            $scope.category = {categoryName: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
        $scope.listFeeds = [];
        $scope.loadListFeeds = function(id){
            Categoryfeeds.getFeeds({id: id},function(result){
                $scope.listFeeds = result;
            });
            angular.element('.listFeed').toggleClass("listFeed-open")
        };
        $scope.settings =function(){
            angular.element('.settings').toggleClass("settings-open");
        };
        $scope.administration =function(){
            angular.element('.administration').toggleClass("administration-open");
        };
    });
