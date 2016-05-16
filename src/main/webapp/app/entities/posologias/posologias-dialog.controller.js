(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PosologiasDialogController', PosologiasDialogController);

    PosologiasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Posologias'];

    function PosologiasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Posologias) {
        var vm = this;
        vm.posologias = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:posologiasUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.posologias.id !== null) {
                Posologias.update(vm.posologias, onSaveSuccess, onSaveError);
            } else {
                Posologias.save(vm.posologias, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
