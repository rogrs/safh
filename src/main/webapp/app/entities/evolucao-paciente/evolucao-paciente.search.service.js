(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('EvolucaoPacienteSearch', EvolucaoPacienteSearch);

    EvolucaoPacienteSearch.$inject = ['$resource'];

    function EvolucaoPacienteSearch($resource) {
        var resourceUrl =  'api/_search/evolucao-pacientes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
