(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('DietasDialogController', DietasDialogController);

    DietasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Dietas'];

    function DietasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Dietas) {
        var vm = this;

        vm.dietas = entity;
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
            if (vm.dietas.id !== null) {
                Dietas.update(vm.dietas, onSaveSuccess, onSaveError);
            } else {
                Dietas.save(vm.dietas, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('safhApp:dietasUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
