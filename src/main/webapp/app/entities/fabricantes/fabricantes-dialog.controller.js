(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('FabricantesDialogController', FabricantesDialogController);

    FabricantesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fabricantes'];

    function FabricantesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Fabricantes) {
        var vm = this;
        vm.fabricantes = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:fabricantesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.fabricantes.id !== null) {
                Fabricantes.update(vm.fabricantes, onSaveSuccess, onSaveError);
            } else {
                Fabricantes.save(vm.fabricantes, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
