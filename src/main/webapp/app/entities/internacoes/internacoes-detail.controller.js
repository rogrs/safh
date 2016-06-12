(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('InternacoesDetailController', InternacoesDetailController);

    InternacoesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Internacoes', 'InternacoesDetalhes', 'Pacientes', 'Clinicas', 'Medicos'];

    function InternacoesDetailController($scope, $rootScope, $stateParams, entity, Internacoes, InternacoesDetalhes, Pacientes, Clinicas, Medicos) {
        var vm = this;

        vm.internacoes = entity;

        var unsubscribe = $rootScope.$on('safhApp:internacoesUpdate', function(event, result) {
            vm.internacoes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
