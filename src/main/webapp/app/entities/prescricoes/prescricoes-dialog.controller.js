(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PrescricoesDialogController', PrescricoesDialogController);

    PrescricoesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Prescricoes'];

    function PrescricoesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Prescricoes) {
        var vm = this;

        vm.prescricoes = entity;
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
            if (vm.prescricoes.id !== null) {
                Prescricoes.update(vm.prescricoes, onSaveSuccess, onSaveError);
            } else {
                Prescricoes.save(vm.prescricoes, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('safhApp:prescricoesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
