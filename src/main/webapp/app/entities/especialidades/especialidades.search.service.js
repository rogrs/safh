(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('EspecialidadesSearch', EspecialidadesSearch);

    EspecialidadesSearch.$inject = ['$resource'];

    function EspecialidadesSearch($resource) {
        var resourceUrl =  'api/_search/especialidades/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
