(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('ClinicasDialogController', ClinicasDialogController);

    ClinicasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Clinicas', 'Leitos', 'Enfermarias'];

    function ClinicasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Clinicas, Leitos, Enfermarias) {
        var vm = this;
        vm.clinicas = entity;
        vm.leitos = Leitos.query();
        vm.enfermarias = Enfermarias.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('safhApp:clinicasUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.clinicas.id !== null) {
                Clinicas.update(vm.clinicas, onSaveSuccess, onSaveError);
            } else {
                Clinicas.save(vm.clinicas, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
