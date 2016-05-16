(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Medicamentos', Medicamentos);

    Medicamentos.$inject = ['$resource'];

    function Medicamentos ($resource) {
        var resourceUrl =  'api/medicamentos/:id';

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
