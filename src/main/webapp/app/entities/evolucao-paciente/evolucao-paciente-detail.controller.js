(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('EvolucaoPacienteDetailController', EvolucaoPacienteDetailController);

    EvolucaoPacienteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'EvolucaoPaciente', 'Pacientes'];

    function EvolucaoPacienteDetailController($scope, $rootScope, $stateParams, entity, EvolucaoPaciente, Pacientes) {
        var vm = this;
        vm.evolucaoPaciente = entity;
        
        var unsubscribe = $rootScope.$on('safhApp:evolucaoPacienteUpdate', function(event, result) {
            vm.evolucaoPaciente = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
