(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('InternacoesSearch', InternacoesSearch);

    InternacoesSearch.$inject = ['$resource'];

    function InternacoesSearch($resource) {
        var resourceUrl =  'api/_search/internacoes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
