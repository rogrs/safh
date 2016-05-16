(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('MedicacaoDialogController', MedicacaoDialogController);

    MedicacaoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Medicacao', 'Pacientes'];

    function MedicacaoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Medicacao, Pacientes) {
        var vm = this;
        vm.medicacao = entity;
        vm.pacientes = Pacientes.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:medicacaoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.medicacao.id !== null) {
                Medicacao.update(vm.medicacao, onSaveSuccess, onSaveError);
            } else {
                Medicacao.save(vm.medicacao, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dataMedicacao = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
