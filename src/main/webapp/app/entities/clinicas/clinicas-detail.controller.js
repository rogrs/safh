(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('ClinicasDetailController', ClinicasDetailController);

    ClinicasDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Clinicas', 'Leitos', 'Enfermarias'];

    function ClinicasDetailController($scope, $rootScope, $stateParams, entity, Clinicas, Leitos, Enfermarias) {
        var vm = this;
        vm.clinicas = entity;
        
        var unsubscribe = $rootScope.$on('safhApp:clinicasUpdate', function(event, result) {
            vm.clinicas = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
