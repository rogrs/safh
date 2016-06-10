(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('InternacoesDeleteController',InternacoesDeleteController);

    InternacoesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Internacoes'];

    function InternacoesDeleteController($uibModalInstance, entity, Internacoes) {
        var vm = this;

        vm.internacoes = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Internacoes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
