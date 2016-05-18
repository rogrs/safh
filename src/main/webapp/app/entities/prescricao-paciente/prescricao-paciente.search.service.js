(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('PrescricaoPacienteSearch', PrescricaoPacienteSearch);

    PrescricaoPacienteSearch.$inject = ['$resource'];

    function PrescricaoPacienteSearch($resource) {
        var resourceUrl =  'api/_search/prescricao-pacientes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
