(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('LeitosDetailController', LeitosDetailController);

    LeitosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Leitos', 'Pacientes'];

    function LeitosDetailController($scope, $rootScope, $stateParams, entity, Leitos, Pacientes) {
        var vm = this;

        vm.leitos = entity;

        var unsubscribe = $rootScope.$on('safhApp:leitosUpdate', function(event, result) {
            vm.leitos = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
