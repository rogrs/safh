(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('InternacaoPacienteDialogController', InternacaoPacienteDialogController);

    InternacaoPacienteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InternacaoPaciente', 'Pacientes', 'Medicos', 'EvolucaoPaciente', 'PrescricaoPaciente'];

    function InternacaoPacienteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InternacaoPaciente, Pacientes, Medicos, EvolucaoPaciente, PrescricaoPaciente) {
        var vm = this;
        vm.internacaoPaciente = entity;
        vm.pacientes = Pacientes.query();
        vm.medicos = Medicos.query();
        vm.evolucaopacientes = EvolucaoPaciente.query();
        vm.prescricaopacientes = PrescricaoPaciente.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:internacaoPacienteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.internacaoPaciente.id !== null) {
                InternacaoPaciente.update(vm.internacaoPaciente, onSaveSuccess, onSaveError);
            } else {
                InternacaoPaciente.save(vm.internacaoPaciente, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dataInternacao = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
