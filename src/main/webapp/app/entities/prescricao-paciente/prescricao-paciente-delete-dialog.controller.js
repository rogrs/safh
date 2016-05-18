(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PrescricaoPacienteDeleteController',PrescricaoPacienteDeleteController);

    PrescricaoPacienteDeleteController.$inject = ['$uibModalInstance', 'entity', 'PrescricaoPaciente'];

    function PrescricaoPacienteDeleteController($uibModalInstance, entity, PrescricaoPaciente) {
        var vm = this;
        vm.prescricaoPaciente = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PrescricaoPaciente.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
