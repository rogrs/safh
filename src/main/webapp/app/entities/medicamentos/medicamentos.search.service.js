(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('MedicamentosSearch', MedicamentosSearch);

    MedicamentosSearch.$inject = ['$resource'];

    function MedicamentosSearch($resource) {
        var resourceUrl =  'api/_search/medicamentos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
