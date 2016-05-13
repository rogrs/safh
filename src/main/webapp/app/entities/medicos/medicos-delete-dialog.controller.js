(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('MedicosDeleteController',MedicosDeleteController);

    MedicosDeleteController.$inject = ['$uibModalInstance', 'entity', 'Medicos'];

    function MedicosDeleteController($uibModalInstance, entity, Medicos) {
        var vm = this;
        vm.medicos = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Medicos.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
