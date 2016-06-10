(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('EspecialidadesDialogController', EspecialidadesDialogController);

    EspecialidadesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Especialidades'];

    function EspecialidadesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Especialidades) {
        var vm = this;

        vm.especialidades = entity;
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
            if (vm.especialidades.id !== null) {
                Especialidades.update(vm.especialidades, onSaveSuccess, onSaveError);
            } else {
                Especialidades.save(vm.especialidades, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('safhApp:especialidadesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
