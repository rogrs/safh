(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('MedicosSearch', MedicosSearch);

    MedicosSearch.$inject = ['$resource'];

    function MedicosSearch($resource) {
        var resourceUrl =  'api/_search/medicos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
