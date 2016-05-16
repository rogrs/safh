(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('MedicacaoSearch', MedicacaoSearch);

    MedicacaoSearch.$inject = ['$resource'];

    function MedicacaoSearch($resource) {
        var resourceUrl =  'api/_search/medicacaos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
