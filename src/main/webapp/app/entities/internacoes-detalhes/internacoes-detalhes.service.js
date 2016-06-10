(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('InternacoesDetalhes', InternacoesDetalhes);

    InternacoesDetalhes.$inject = ['$resource', 'DateUtils'];

    function InternacoesDetalhes ($resource, DateUtils) {
        var resourceUrl =  'api/internacoes-detalhes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dataDetalhe = DateUtils.convertLocalDateFromServer(data.dataDetalhe);
                        data.horario = DateUtils.convertLocalDateFromServer(data.horario);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dataDetalhe = DateUtils.convertLocalDateToServer(data.dataDetalhe);
                    data.horario = DateUtils.convertLocalDateToServer(data.horario);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dataDetalhe = DateUtils.convertLocalDateToServer(data.dataDetalhe);
                    data.horario = DateUtils.convertLocalDateToServer(data.horario);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
