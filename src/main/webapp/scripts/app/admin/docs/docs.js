'use strict';

angular.module('readhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('docs', {
                parent: 'admin',
                url: '/docs',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'global.menu.admin.apidocs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/docs/docs.html'
                    },'sidebar@':{
                        templateUrl: 'scripts/components/sidebar/sidebar.html',
                        controller: 'SidebarController'
                    }
                }
            });
    });
