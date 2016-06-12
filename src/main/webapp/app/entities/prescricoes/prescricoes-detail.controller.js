(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PrescricoesDetailController', PrescricoesDetailController);

    PrescricoesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Prescricoes', 'InternacoesDetalhes'];

    function PrescricoesDetailController($scope, $rootScope, $stateParams, entity, Prescricoes, InternacoesDetalhes) {
        var vm = this;

        vm.prescricoes = entity;

        var unsubscribe = $rootScope.$on('safhApp:prescricoesUpdate', function(event, result) {
            vm.prescricoes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
