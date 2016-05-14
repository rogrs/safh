(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('LeitosDeleteController',LeitosDeleteController);

    LeitosDeleteController.$inject = ['$uibModalInstance', 'entity', 'Leitos'];

    function LeitosDeleteController($uibModalInstance, entity, Leitos) {
        var vm = this;
        vm.leitos = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Leitos.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
