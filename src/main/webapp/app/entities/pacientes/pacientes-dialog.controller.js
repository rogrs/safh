(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PacientesDialogController', PacientesDialogController);

    PacientesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pacientes'];

    function PacientesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Pacientes) {
        var vm = this;
        vm.pacientes = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:pacientesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.pacientes.id !== null) {
                Pacientes.update(vm.pacientes, onSaveSuccess, onSaveError);
            } else {
                Pacientes.save(vm.pacientes, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.nascimento = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
