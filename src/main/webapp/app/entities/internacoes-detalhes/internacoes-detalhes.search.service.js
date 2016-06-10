(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('InternacoesDetalhesSearch', InternacoesDetalhesSearch);

    InternacoesDetalhesSearch.$inject = ['$resource'];

    function InternacoesDetalhesSearch($resource) {
        var resourceUrl =  'api/_search/internacoes-detalhes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
