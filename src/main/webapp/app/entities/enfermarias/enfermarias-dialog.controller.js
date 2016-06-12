(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('EnfermariasDialogController', EnfermariasDialogController);

    EnfermariasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Enfermarias', 'Pacientes'];

    function EnfermariasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Enfermarias, Pacientes) {
        var vm = this;

        vm.enfermarias = entity;
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
            if (vm.enfermarias.id !== null) {
                Enfermarias.update(vm.enfermarias, onSaveSuccess, onSaveError);
            } else {
                Enfermarias.save(vm.enfermarias, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('safhApp:enfermariasUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
