(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('MedicamentosDeleteController',MedicamentosDeleteController);

    MedicamentosDeleteController.$inject = ['$uibModalInstance', 'entity', 'Medicamentos'];

    function MedicamentosDeleteController($uibModalInstance, entity, Medicamentos) {
        var vm = this;
        vm.medicamentos = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Medicamentos.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
