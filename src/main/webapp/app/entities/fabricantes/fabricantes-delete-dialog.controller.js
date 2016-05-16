(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('FabricantesDeleteController',FabricantesDeleteController);

    FabricantesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Fabricantes'];

    function FabricantesDeleteController($uibModalInstance, entity, Fabricantes) {
        var vm = this;
        vm.fabricantes = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Fabricantes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
