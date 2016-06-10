(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PrescricoesDeleteController',PrescricoesDeleteController);

    PrescricoesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Prescricoes'];

    function PrescricoesDeleteController($uibModalInstance, entity, Prescricoes) {
        var vm = this;

        vm.prescricoes = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Prescricoes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
