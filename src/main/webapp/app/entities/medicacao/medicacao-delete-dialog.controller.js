(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('MedicacaoDeleteController',MedicacaoDeleteController);

    MedicacaoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Medicacao'];

    function MedicacaoDeleteController($uibModalInstance, entity, Medicacao) {
        var vm = this;
        vm.medicacao = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Medicacao.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
