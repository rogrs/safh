(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('InternacoesDialogController', InternacoesDialogController);

    InternacoesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Internacoes', 'InternacoesDetalhes', 'Pacientes', 'Clinicas', 'Medicos'];

    function InternacoesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Internacoes, InternacoesDetalhes, Pacientes, Clinicas, Medicos) {
        var vm = this;

        vm.internacoes = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.internacoesdetalhes = InternacoesDetalhes.query();
        vm.pacientes = Pacientes.query();
        vm.clinicas = Clinicas.query();
        vm.medicos = Medicos.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.internacoes.id !== null) {
                Internacoes.update(vm.internacoes, onSaveSuccess, onSaveError);
            } else {
                Internacoes.save(vm.internacoes, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('safhApp:internacoesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dataInternacao = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
