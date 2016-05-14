(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('EnfermariasDeleteController',EnfermariasDeleteController);

    EnfermariasDeleteController.$inject = ['$uibModalInstance', 'entity', 'Enfermarias'];

    function EnfermariasDeleteController($uibModalInstance, entity, Enfermarias) {
        var vm = this;
        vm.enfermarias = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Enfermarias.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
