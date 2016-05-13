(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('DietasDialogController', DietasDialogController);

    DietasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Dietas'];

    function DietasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Dietas) {
        var vm = this;
        vm.dietas = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:dietasUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.dietas.id !== null) {
                Dietas.update(vm.dietas, onSaveSuccess, onSaveError);
            } else {
                Dietas.save(vm.dietas, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
