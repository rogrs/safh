(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('DietasSearch', DietasSearch);

    DietasSearch.$inject = ['$resource'];

    function DietasSearch($resource) {
        var resourceUrl =  'api/_search/dietas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
