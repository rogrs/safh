(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PosologiasDeleteController',PosologiasDeleteController);

    PosologiasDeleteController.$inject = ['$uibModalInstance', 'entity', 'Posologias'];

    function PosologiasDeleteController($uibModalInstance, entity, Posologias) {
        var vm = this;
        vm.posologias = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Posologias.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
