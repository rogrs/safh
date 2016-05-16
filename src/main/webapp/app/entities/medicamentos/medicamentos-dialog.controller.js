(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('MedicamentosDialogController', MedicamentosDialogController);

    MedicamentosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Medicamentos', 'Fabricantes', 'Posologia'];

    function MedicamentosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Medicamentos, Fabricantes, Posologia) {
        var vm = this;
        vm.medicamentos = entity;
        vm.fabricantes = Fabricantes.query();
        vm.posologias = Posologia.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:medicamentosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.medicamentos.id !== null) {
                Medicamentos.update(vm.medicamentos, onSaveSuccess, onSaveError);
            } else {
                Medicamentos.save(vm.medicamentos, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
