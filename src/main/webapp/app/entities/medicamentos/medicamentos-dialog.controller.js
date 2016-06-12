(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('MedicamentosDialogController', MedicamentosDialogController);

    MedicamentosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Medicamentos', 'Posologias', 'Fabricantes'];

    function MedicamentosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Medicamentos, Posologias, Fabricantes) {
        var vm = this;

        vm.medicamentos = entity;
        vm.clear = clear;
        vm.save = save;
        vm.posologias = Posologias.query();
        vm.fabricantes = Fabricantes.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.medicamentos.id !== null) {
                Medicamentos.update(vm.medicamentos, onSaveSuccess, onSaveError);
            } else {
                Medicamentos.save(vm.medicamentos, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('safhApp:medicamentosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
