(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('ClinicasDeleteController',ClinicasDeleteController);

    ClinicasDeleteController.$inject = ['$uibModalInstance', 'entity', 'Clinicas'];

    function ClinicasDeleteController($uibModalInstance, entity, Clinicas) {
        var vm = this;
        vm.clinicas = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Clinicas.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
