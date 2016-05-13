(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('DietasDeleteController',DietasDeleteController);

    DietasDeleteController.$inject = ['$uibModalInstance', 'entity', 'Dietas'];

    function DietasDeleteController($uibModalInstance, entity, Dietas) {
        var vm = this;
        vm.dietas = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Dietas.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
