(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('InternacaoPacienteDetailController', InternacaoPacienteDetailController);

    InternacaoPacienteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InternacaoPaciente', 'Pacientes', 'Medicos', 'EvolucaoPaciente', 'PrescricaoPaciente'];

    function InternacaoPacienteDetailController($scope, $rootScope, $stateParams, entity, InternacaoPaciente, Pacientes, Medicos, EvolucaoPaciente, PrescricaoPaciente) {
        var vm = this;
        vm.internacaoPaciente = entity;
        
        var unsubscribe = $rootScope.$on('safhApp:internacaoPacienteUpdate', function(event, result) {
            vm.internacaoPaciente = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
