
angular.module('readhubApp')
    .factory('Categoryfeeds', function ($resource) {
        return $resource('api/categoryfeeds/:id', {}, {
            'getFeeds': {
                method: 'GET',
                isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
