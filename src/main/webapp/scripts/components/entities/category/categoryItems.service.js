
angular.module('readhubApp')
    .factory('CategoryItems', function ($resource) {
        return $resource('api/categoryitems/:id', {}, {
            'get': {
                method: 'GET',
                isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
