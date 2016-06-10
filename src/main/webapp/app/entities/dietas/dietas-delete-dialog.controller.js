(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('DietasDeleteController',DietasDeleteController);

    DietasDeleteController.$inject = ['$uibModalInstance', 'entity', 'Dietas'];

    function DietasDeleteController($uibModalInstance, entity, Dietas) {
        var vm = this;

        vm.dietas = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Dietas.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
