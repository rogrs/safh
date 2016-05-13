(function() {
    'use strict';
    angular
        .module('safhApp')
        .factory('Pacientes', Pacientes);

    Pacientes.$inject = ['$resource', 'DateUtils'];

    function Pacientes ($resource, DateUtils) {
        var resourceUrl =  'api/pacientes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.nascimento = DateUtils.convertLocalDateFromServer(data.nascimento);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.nascimento = DateUtils.convertLocalDateToServer(data.nascimento);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.nascimento = DateUtils.convertLocalDateToServer(data.nascimento);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
