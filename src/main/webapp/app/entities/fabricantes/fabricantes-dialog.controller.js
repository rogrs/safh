(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('FabricantesDialogController', FabricantesDialogController);

    FabricantesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fabricantes'];

    function FabricantesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Fabricantes) {
        var vm = this;

        vm.fabricantes = entity;
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
            if (vm.fabricantes.id !== null) {
                Fabricantes.update(vm.fabricantes, onSaveSuccess, onSaveError);
            } else {
                Fabricantes.save(vm.fabricantes, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('safhApp:fabricantesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
