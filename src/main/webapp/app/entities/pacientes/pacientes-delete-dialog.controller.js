(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PacientesDeleteController',PacientesDeleteController);

    PacientesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pacientes'];

    function PacientesDeleteController($uibModalInstance, entity, Pacientes) {
        var vm = this;

        vm.pacientes = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Pacientes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
