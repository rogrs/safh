(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('MedicamentosDeleteController',MedicamentosDeleteController);

    MedicamentosDeleteController.$inject = ['$uibModalInstance', 'entity', 'Medicamentos'];

    function MedicamentosDeleteController($uibModalInstance, entity, Medicamentos) {
        var vm = this;

        vm.medicamentos = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Medicamentos.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
