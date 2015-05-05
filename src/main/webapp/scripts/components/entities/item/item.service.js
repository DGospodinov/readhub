'use strict';

angular.module('readhubApp')
    .factory('Item', function ($resource) {
        return $resource('api/items/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.pubDate = new Date(data.pubDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
