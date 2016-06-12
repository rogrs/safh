(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('LeitosDialogController', LeitosDialogController);

    LeitosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Leitos', 'Pacientes'];

    function LeitosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Leitos, Pacientes) {
        var vm = this;

        vm.leitos = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pacientes = Pacientes.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.leitos.id !== null) {
                Leitos.update(vm.leitos, onSaveSuccess, onSaveError);
            } else {
                Leitos.save(vm.leitos, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('safhApp:leitosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
