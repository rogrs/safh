(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('EvolucaoPacienteDialogController', EvolucaoPacienteDialogController);

    EvolucaoPacienteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EvolucaoPaciente', 'Pacientes'];

    function EvolucaoPacienteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EvolucaoPaciente, Pacientes) {
        var vm = this;
        vm.evolucaoPaciente = entity;
        vm.pacientes = Pacientes.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:evolucaoPacienteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.evolucaoPaciente.id !== null) {
                EvolucaoPaciente.update(vm.evolucaoPaciente, onSaveSuccess, onSaveError);
            } else {
                EvolucaoPaciente.save(vm.evolucaoPaciente, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dataEvolucao = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
