(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Medicacao', Medicacao);

    Medicacao.$inject = ['$resource', 'DateUtils'];

    function Medicacao ($resource, DateUtils) {
        var resourceUrl =  'api/medicacaos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dataMedicacao = DateUtils.convertLocalDateFromServer(data.dataMedicacao);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dataMedicacao = DateUtils.convertLocalDateToServer(data.dataMedicacao);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dataMedicacao = DateUtils.convertLocalDateToServer(data.dataMedicacao);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
