(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('EvolucaoPacienteDeleteController',EvolucaoPacienteDeleteController);

    EvolucaoPacienteDeleteController.$inject = ['$uibModalInstance', 'entity', 'EvolucaoPaciente'];

    function EvolucaoPacienteDeleteController($uibModalInstance, entity, EvolucaoPaciente) {
        var vm = this;
        vm.evolucaoPaciente = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            EvolucaoPaciente.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
