(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Medicos', Medicos);

    Medicos.$inject = ['$resource'];

    function Medicos ($resource) {
        var resourceUrl =  'api/medicos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
