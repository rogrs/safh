(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('InternacaoPaciente', InternacaoPaciente);

    InternacaoPaciente.$inject = ['$resource', 'DateUtils'];

    function InternacaoPaciente ($resource, DateUtils) {
        var resourceUrl =  'api/internacao-pacientes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dataInternacao = DateUtils.convertLocalDateFromServer(data.dataInternacao);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dataInternacao = DateUtils.convertLocalDateToServer(data.dataInternacao);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dataInternacao = DateUtils.convertLocalDateToServer(data.dataInternacao);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
