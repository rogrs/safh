(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('FabricantesDetailController', FabricantesDetailController);

    FabricantesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Fabricantes', 'Medicamentos'];

    function FabricantesDetailController($scope, $rootScope, $stateParams, entity, Fabricantes, Medicamentos) {
        var vm = this;

        vm.fabricantes = entity;

        var unsubscribe = $rootScope.$on('safhApp:fabricantesUpdate', function(event, result) {
            vm.fabricantes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
