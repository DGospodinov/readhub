'use strict';

angular.module('readhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('register', {
                parent: 'account',
                url: '/register',
                data: {
                    roles: [],
                    pageTitle: 'register.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/register/register.html',
                        controller: 'RegisterController'
                    },
                    'sidebar@':{
                        templateUrl: 'scripts/components/sidebar/sidebar.html',
                        controller: 'SidebarController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('register');
                        return $translate.refresh();
                    }]
                }
            });
    });
