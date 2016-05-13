(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Dietas', Dietas);

    Dietas.$inject = ['$resource'];

    function Dietas ($resource) {
        var resourceUrl =  'api/dietas/:id';

        return $resource(resourceUrl, {}, {
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
    }
})();
