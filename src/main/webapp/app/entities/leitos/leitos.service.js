(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Leitos', Leitos);

    Leitos.$inject = ['$resource'];

    function Leitos ($resource) {
        var resourceUrl =  'api/leitos/:id';

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
