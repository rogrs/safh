(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PacientesDetailController', PacientesDetailController);

    PacientesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Pacientes', 'Internacoes', 'Clinicas', 'Enfermarias', 'Leitos'];

    function PacientesDetailController($scope, $rootScope, $stateParams, entity, Pacientes, Internacoes, Clinicas, Enfermarias, Leitos) {
        var vm = this;

        vm.pacientes = entity;

        var unsubscribe = $rootScope.$on('safhApp:pacientesUpdate', function(event, result) {
            vm.pacientes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
