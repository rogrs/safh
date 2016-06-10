(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Clinicas', Clinicas);

    Clinicas.$inject = ['$resource'];

    function Clinicas ($resource) {
        var resourceUrl =  'api/clinicas/:id';

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
