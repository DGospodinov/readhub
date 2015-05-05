'use strict';

angular.module('readhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('feed', {
                parent: 'entity',
                url: '/feed',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'readhubApp.feed.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/feed/feeds.html',
                        controller: 'FeedController'
                    },
                    'sidebar@':{
                        templateUrl: 'scripts/components/sidebar/sidebar.html',
                        controller: 'SidebarController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('feed');
                        return $translate.refresh();
                    }]
                }
            })
            .state('feedDetail', {
                parent: 'entity',
                url: '/feed/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'readhubApp.feed.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/feed/feed-detail.html',
                        controller: 'FeedDetailController'
                    },
                    'sidebar@':{
                        templateUrl: 'scripts/components/sidebar/sidebar.html',
                        controller: 'SidebarController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('feed');
                        return $translate.refresh();
                    }]
                }
            });
    });
