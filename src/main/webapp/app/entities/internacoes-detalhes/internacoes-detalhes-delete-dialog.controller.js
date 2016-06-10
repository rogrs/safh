(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('InternacoesDetalhesDeleteController',InternacoesDetalhesDeleteController);

    InternacoesDetalhesDeleteController.$inject = ['$uibModalInstance', 'entity', 'InternacoesDetalhes'];

    function InternacoesDetalhesDeleteController($uibModalInstance, entity, InternacoesDetalhes) {
        var vm = this;

        vm.internacoesDetalhes = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InternacoesDetalhes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
