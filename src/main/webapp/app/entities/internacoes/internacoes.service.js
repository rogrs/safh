(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Internacoes', Internacoes);

    Internacoes.$inject = ['$resource', 'DateUtils'];

    function Internacoes ($resource, DateUtils) {
        var resourceUrl =  'api/internacoes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dataInternacao = DateUtils.convertLocalDateFromServer(data.dataInternacao);
                    }
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
