(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('EspecialidadesDeleteController',EspecialidadesDeleteController);

    EspecialidadesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Especialidades'];

    function EspecialidadesDeleteController($uibModalInstance, entity, Especialidades) {
        var vm = this;

        vm.especialidades = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Especialidades.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
