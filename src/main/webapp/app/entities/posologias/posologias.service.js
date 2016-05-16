(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Posologias', Posologias);

    Posologias.$inject = ['$resource'];

    function Posologias ($resource) {
        var resourceUrl =  'api/posologias/:id';

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
