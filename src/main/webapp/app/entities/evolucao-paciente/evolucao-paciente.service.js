(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('EvolucaoPaciente', EvolucaoPaciente);

    EvolucaoPaciente.$inject = ['$resource', 'DateUtils'];

    function EvolucaoPaciente ($resource, DateUtils) {
        var resourceUrl =  'api/evolucao-pacientes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dataEvolucao = DateUtils.convertLocalDateFromServer(data.dataEvolucao);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dataEvolucao = DateUtils.convertLocalDateToServer(data.dataEvolucao);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dataEvolucao = DateUtils.convertLocalDateToServer(data.dataEvolucao);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
