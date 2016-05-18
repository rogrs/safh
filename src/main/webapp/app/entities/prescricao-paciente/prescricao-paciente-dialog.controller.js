(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PrescricaoPacienteDialogController', PrescricaoPacienteDialogController);

    PrescricaoPacienteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PrescricaoPaciente', 'Pacientes', 'Medicamentos', 'Posologias', 'Dietas'];

    function PrescricaoPacienteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PrescricaoPaciente, Pacientes, Medicamentos, Posologias, Dietas) {
        var vm = this;
        vm.prescricaoPaciente = entity;
        vm.pacientes = Pacientes.query();
        vm.medicamentos = Medicamentos.query();
        vm.posologias = Posologias.query();
        vm.dietas = Dietas.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:prescricaoPacienteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.prescricaoPaciente.id !== null) {
                PrescricaoPaciente.update(vm.prescricaoPaciente, onSaveSuccess, onSaveError);
            } else {
                PrescricaoPaciente.save(vm.prescricaoPaciente, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dataEntrada = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
