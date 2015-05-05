'use strict';

angular.module('readhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('readlater', {
                parent: 'entity',
                url: '/readlater',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'readhubApp.readlater.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/readlater/readlaters.html',
                        controller: 'ReadlaterController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('readlater');
                        return $translate.refresh();
                    }]
                }
            })
            .state('readlaterDetail', {
                parent: 'entity',
                url: '/readlater/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'readhubApp.readlater.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/readlater/readlater-detail.html',
                        controller: 'ReadlaterDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('readlater');
                        return $translate.refresh();
                    }]
                }
            });
    });
