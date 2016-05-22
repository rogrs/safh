(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('InternacaoPacienteDeleteController',InternacaoPacienteDeleteController);

    InternacaoPacienteDeleteController.$inject = ['$uibModalInstance', 'entity', 'InternacaoPaciente'];

    function InternacaoPacienteDeleteController($uibModalInstance, entity, InternacaoPaciente) {
        var vm = this;
        vm.internacaoPaciente = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InternacaoPaciente.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
