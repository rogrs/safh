(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('PosologiasSearch', PosologiasSearch);

    PosologiasSearch.$inject = ['$resource'];

    function PosologiasSearch($resource) {
        var resourceUrl =  'api/_search/posologias/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
