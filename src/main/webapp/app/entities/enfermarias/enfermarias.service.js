(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Enfermarias', Enfermarias);

    Enfermarias.$inject = ['$resource'];

    function Enfermarias ($resource) {
        var resourceUrl =  'api/enfermarias/:id';

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
