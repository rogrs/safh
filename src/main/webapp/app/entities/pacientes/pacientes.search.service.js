(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('PacientesSearch', PacientesSearch);

    PacientesSearch.$inject = ['$resource'];

    function PacientesSearch($resource) {
        var resourceUrl =  'api/_search/pacientes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
