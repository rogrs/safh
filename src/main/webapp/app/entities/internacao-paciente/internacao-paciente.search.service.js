(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('InternacaoPacienteSearch', InternacaoPacienteSearch);

    InternacaoPacienteSearch.$inject = ['$resource'];

    function InternacaoPacienteSearch($resource) {
        var resourceUrl =  'api/_search/internacao-pacientes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
