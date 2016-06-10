(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('PrescricoesSearch', PrescricoesSearch);

    PrescricoesSearch.$inject = ['$resource'];

    function PrescricoesSearch($resource) {
        var resourceUrl =  'api/_search/prescricoes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
