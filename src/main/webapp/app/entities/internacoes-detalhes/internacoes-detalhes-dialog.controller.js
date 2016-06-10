(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('InternacoesDetalhesDialogController', InternacoesDetalhesDialogController);

    InternacoesDetalhesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InternacoesDetalhes', 'Internacoes', 'Dietas', 'Prescricoes', 'Posologias'];

    function InternacoesDetalhesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InternacoesDetalhes, Internacoes, Dietas, Prescricoes, Posologias) {
        var vm = this;

        vm.internacoesDetalhes = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.internacoes = Internacoes.query();
        vm.dietas = Dietas.query();
        vm.prescricoes = Prescricoes.query();
        vm.posologias = Posologias.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.internacoesDetalhes.id !== null) {
                InternacoesDetalhes.update(vm.internacoesDetalhes, onSaveSuccess, onSaveError);
            } else {
                InternacoesDetalhes.save(vm.internacoesDetalhes, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('safhApp:internacoesDetalhesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dataDetalhe = false;
        vm.datePickerOpenStatus.horario = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
