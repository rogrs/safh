(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PrescricaoPacienteDetailController', PrescricaoPacienteDetailController);

    PrescricaoPacienteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PrescricaoPaciente', 'Pacientes', 'Medicamentos', 'Posologias', 'Dietas'];

    function PrescricaoPacienteDetailController($scope, $rootScope, $stateParams, entity, PrescricaoPaciente, Pacientes, Medicamentos, Posologias, Dietas) {
        var vm = this;
        vm.prescricaoPaciente = entity;
        
        var unsubscribe = $rootScope.$on('safhApp:prescricaoPacienteUpdate', function(event, result) {
            vm.prescricaoPaciente = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
