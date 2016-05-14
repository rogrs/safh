(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('EnfermariasDialogController', EnfermariasDialogController);

    EnfermariasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Enfermarias'];

    function EnfermariasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Enfermarias) {
        var vm = this;
        vm.enfermarias = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:enfermariasUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.enfermarias.id !== null) {
                Enfermarias.update(vm.enfermarias, onSaveSuccess, onSaveError);
            } else {
                Enfermarias.save(vm.enfermarias, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
