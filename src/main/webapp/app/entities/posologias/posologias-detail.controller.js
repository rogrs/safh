(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PosologiasDetailController', PosologiasDetailController);

    PosologiasDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Posologias'];

    function PosologiasDetailController($scope, $rootScope, $stateParams, entity, Posologias) {
        var vm = this;
        vm.posologias = entity;
        
        var unsubscribe = $rootScope.$on('safhApp:posologiasUpdate', function(event, result) {
            vm.posologias = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
