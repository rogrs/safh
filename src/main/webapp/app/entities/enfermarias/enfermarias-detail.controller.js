(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('EnfermariasDetailController', EnfermariasDetailController);

    EnfermariasDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Enfermarias', 'Pacientes'];

    function EnfermariasDetailController($scope, $rootScope, $stateParams, entity, Enfermarias, Pacientes) {
        var vm = this;

        vm.enfermarias = entity;

        var unsubscribe = $rootScope.$on('safhApp:enfermariasUpdate', function(event, result) {
            vm.enfermarias = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();