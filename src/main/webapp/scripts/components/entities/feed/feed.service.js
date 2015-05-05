'use strict';

angular.module('readhubApp')
    .factory('Feed', function ($resource) {
        return $resource('api/feeds/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
    .factory('FeedItems',function($resource){
        return $resource('api/feeds/:id/items',{},{
            get: {
                method:'GET',
                isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.pubDate = new Date(data.pubDate);
                    return data;
                }
            }
        })
    });

