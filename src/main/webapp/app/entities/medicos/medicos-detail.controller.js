(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('MedicosDetailController', MedicosDetailController);

    MedicosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Medicos', 'Especialidades'];

    function MedicosDetailController($scope, $rootScope, $stateParams, entity, Medicos, Especialidades) {
        var vm = this;

        vm.medicos = entity;

        var unsubscribe = $rootScope.$on('safhApp:medicosUpdate', function(event, result) {
            vm.medicos = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
