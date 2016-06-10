(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('FabricantesDeleteController',FabricantesDeleteController);

    FabricantesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Fabricantes'];

    function FabricantesDeleteController($uibModalInstance, entity, Fabricantes) {
        var vm = this;

        vm.fabricantes = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Fabricantes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
