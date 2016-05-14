(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('EnfermariasSearch', EnfermariasSearch);

    EnfermariasSearch.$inject = ['$resource'];

    function EnfermariasSearch($resource) {
        var resourceUrl =  'api/_search/enfermarias/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
