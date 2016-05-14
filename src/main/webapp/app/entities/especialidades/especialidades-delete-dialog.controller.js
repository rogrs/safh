(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('EspecialidadesDeleteController',EspecialidadesDeleteController);

    EspecialidadesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Especialidades'];

    function EspecialidadesDeleteController($uibModalInstance, entity, Especialidades) {
        var vm = this;
        vm.especialidades = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Especialidades.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
