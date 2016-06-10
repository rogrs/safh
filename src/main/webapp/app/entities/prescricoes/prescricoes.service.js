(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Prescricoes', Prescricoes);

    Prescricoes.$inject = ['$resource'];

    function Prescricoes ($resource) {
        var resourceUrl =  'api/prescricoes/:id';

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
