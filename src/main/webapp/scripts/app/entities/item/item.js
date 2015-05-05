'use strict';

angular.module('readhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('item', {
                parent: 'entity',
                url: '/item',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'readhubApp.item.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/item/items.html',
                        controller: 'ItemController'
                    },
                    'sidebar@':{
                        templateUrl: 'scripts/components/sidebar/sidebar.html',
                        controller: 'SidebarController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('item');
                        return $translate.refresh();

                    }]
                }
            })
            .state('itemDetail', {
                parent: 'entity',
                url: '/item/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'readhubApp.item.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/item/item-detail.html',
                        controller: 'ItemDetailController'
                    },
                    'sidebar@':{
                        templateUrl: 'scripts/components/sidebar/sidebar.html',
                        controller: 'SidebarController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('item');
                        return $translate.refresh();
                    }]
                }
            });
    });
