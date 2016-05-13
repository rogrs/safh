(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('MedicosDialogController', MedicosDialogController);

    MedicosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Medicos'];

    function MedicosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Medicos) {
        var vm = this;
        vm.medicos = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:medicosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.medicos.id !== null) {
                Medicos.update(vm.medicos, onSaveSuccess, onSaveError);
            } else {
                Medicos.save(vm.medicos, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
