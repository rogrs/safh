(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('MedicosDialogController', MedicosDialogController);

    MedicosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Medicos', 'Especialidades'];

    function MedicosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Medicos, Especialidades) {
        var vm = this;

        vm.medicos = entity;
        vm.clear = clear;
        vm.save = save;
        vm.especialidades = Especialidades.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.medicos.id !== null) {
                Medicos.update(vm.medicos, onSaveSuccess, onSaveError);
            } else {
                Medicos.save(vm.medicos, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('safhApp:medicosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
