(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('DietasDetailController', DietasDetailController);

    DietasDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Dietas'];

    function DietasDetailController($scope, $rootScope, $stateParams, entity, Dietas) {
        var vm = this;
        vm.dietas = entity;
        
        var unsubscribe = $rootScope.$on('safhApp:dietasUpdate', function(event, result) {
            vm.dietas = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
