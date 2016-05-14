(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Especialidades', Especialidades);

    Especialidades.$inject = ['$resource'];

    function Especialidades ($resource) {
        var resourceUrl =  'api/especialidades/:id';

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
