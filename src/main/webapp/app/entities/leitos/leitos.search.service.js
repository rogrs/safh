(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('LeitosSearch', LeitosSearch);

    LeitosSearch.$inject = ['$resource'];

    function LeitosSearch($resource) {
        var resourceUrl =  'api/_search/leitos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
