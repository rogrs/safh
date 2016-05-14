(function() {
    'use strict';

    angular
        .module('safhApp')
        .factory('ClinicasSearch', ClinicasSearch);

    ClinicasSearch.$inject = ['$resource'];

    function ClinicasSearch($resource) {
        var resourceUrl =  'api/_search/clinicas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
