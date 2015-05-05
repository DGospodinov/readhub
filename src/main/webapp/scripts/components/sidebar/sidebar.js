'use strict';

angular.module('readhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sidebar', {
                parent: 'site',
                url: '/sidebar',
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
                }
            })
    });
