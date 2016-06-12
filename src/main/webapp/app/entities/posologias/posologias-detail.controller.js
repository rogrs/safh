(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PosologiasDetailController', PosologiasDetailController);

    PosologiasDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Posologias', 'Medicamentos', 'InternacoesDetalhes'];

    function PosologiasDetailController($scope, $rootScope, $stateParams, entity, Posologias, Medicamentos, InternacoesDetalhes) {
        var vm = this;

        vm.posologias = entity;

        var unsubscribe = $rootScope.$on('safhApp:posologiasUpdate', function(event, result) {
            vm.posologias = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
