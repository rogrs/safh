(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PosologiasDialogController', PosologiasDialogController);

    PosologiasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Posologias'];

    function PosologiasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Posologias) {
        var vm = this;

        vm.posologias = entity;
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
            if (vm.posologias.id !== null) {
                Posologias.update(vm.posologias, onSaveSuccess, onSaveError);
            } else {
                Posologias.save(vm.posologias, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('safhApp:posologiasUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
