(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('ClinicasDialogController', ClinicasDialogController);

    ClinicasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Clinicas'];

    function ClinicasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Clinicas) {
        var vm = this;

        vm.clinicas = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.clinicas.id !== null) {
                Clinicas.update(vm.clinicas, onSaveSuccess, onSaveError);
            } else {
                Clinicas.save(vm.clinicas, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('safhApp:clinicasUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
