(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('FabricantesDetailController', FabricantesDetailController);

    FabricantesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Fabricantes'];

    function FabricantesDetailController($scope, $rootScope, $stateParams, entity, Fabricantes) {
        var vm = this;

        vm.fabricantes = entity;

        var unsubscribe = $rootScope.$on('safhApp:fabricantesUpdate', function(event, result) {
            vm.fabricantes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
