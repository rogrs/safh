(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('EspecialidadesDetailController', EspecialidadesDetailController);

    EspecialidadesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Especialidades'];

    function EspecialidadesDetailController($scope, $rootScope, $stateParams, entity, Especialidades) {
        var vm = this;

        vm.especialidades = entity;

        var unsubscribe = $rootScope.$on('safhApp:especialidadesUpdate', function(event, result) {
            vm.especialidades = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
