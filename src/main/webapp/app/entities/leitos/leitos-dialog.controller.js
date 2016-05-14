(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('LeitosDialogController', LeitosDialogController);

    LeitosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Leitos'];

    function LeitosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Leitos) {
        var vm = this;
        vm.leitos = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:leitosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.leitos.id !== null) {
                Leitos.update(vm.leitos, onSaveSuccess, onSaveError);
            } else {
                Leitos.save(vm.leitos, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
