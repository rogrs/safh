(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('MedicamentosDetailController', MedicamentosDetailController);

    MedicamentosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Medicamentos', 'Fabricantes'];

    function MedicamentosDetailController($scope, $rootScope, $stateParams, entity, Medicamentos, Fabricantes) {
        var vm = this;
        vm.medicamentos = entity;
        
        var unsubscribe = $rootScope.$on('safhApp:medicamentosUpdate', function(event, result) {
            vm.medicamentos = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();