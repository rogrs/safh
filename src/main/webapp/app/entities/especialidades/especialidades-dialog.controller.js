(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('EspecialidadesDialogController', EspecialidadesDialogController);

    EspecialidadesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Especialidades'];

    function EspecialidadesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Especialidades) {
        var vm = this;
        vm.especialidades = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:especialidadesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.especialidades.id !== null) {
                Especialidades.update(vm.especialidades, onSaveSuccess, onSaveError);
            } else {
                Especialidades.save(vm.especialidades, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
