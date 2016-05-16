(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Fabricantes', Fabricantes);

    Fabricantes.$inject = ['$resource'];

    function Fabricantes ($resource) {
        var resourceUrl =  'api/fabricantes/:id';

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
