(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Pacientes', Pacientes);

    Pacientes.$inject = ['$resource'];

    function Pacientes ($resource) {
        var resourceUrl =  'api/pacientes/:id';

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
