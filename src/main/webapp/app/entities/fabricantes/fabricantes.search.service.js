(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('FabricantesSearch', FabricantesSearch);

    FabricantesSearch.$inject = ['$resource'];

    function FabricantesSearch($resource) {
        var resourceUrl =  'api/_search/fabricantes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
