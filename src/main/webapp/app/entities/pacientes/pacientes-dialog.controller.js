(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PacientesDialogController', PacientesDialogController);

    PacientesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pacientes', 'Internacoes', 'Clinicas', 'Enfermarias', 'Leitos'];

    function PacientesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Pacientes, Internacoes, Clinicas, Enfermarias, Leitos) {
        var vm = this;

        vm.pacientes = entity;
        vm.clear = clear;
        vm.save = save;
        vm.internacoes = Internacoes.query();
        vm.clinicas = Clinicas.query();
        vm.enfermarias = Enfermarias.query();
        vm.leitos = Leitos.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pacientes.id !== null) {
                Pacientes.update(vm.pacientes, onSaveSuccess, onSaveError);
            } else {
                Pacientes.save(vm.pacientes, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('safhApp:pacientesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
