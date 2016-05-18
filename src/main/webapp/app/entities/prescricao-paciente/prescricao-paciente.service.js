(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('PrescricaoPaciente', PrescricaoPaciente);

    PrescricaoPaciente.$inject = ['$resource', 'DateUtils'];

    function PrescricaoPaciente ($resource, DateUtils) {
        var resourceUrl =  'api/prescricao-pacientes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dataEntrada = DateUtils.convertLocalDateFromServer(data.dataEntrada);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dataEntrada = DateUtils.convertLocalDateToServer(data.dataEntrada);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dataEntrada = DateUtils.convertLocalDateToServer(data.dataEntrada);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
