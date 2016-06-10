(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('InternacoesDetalhesDetailController', InternacoesDetalhesDetailController);

    InternacoesDetalhesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InternacoesDetalhes', 'Internacoes', 'Dietas', 'Prescricoes', 'Posologias'];

    function InternacoesDetalhesDetailController($scope, $rootScope, $stateParams, entity, InternacoesDetalhes, Internacoes, Dietas, Prescricoes, Posologias) {
        var vm = this;

        vm.internacoesDetalhes = entity;

        var unsubscribe = $rootScope.$on('safhApp:internacoesDetalhesUpdate', function(event, result) {
            vm.internacoesDetalhes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
