(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('MedicacaoDetailController', MedicacaoDetailController);

    MedicacaoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Medicacao', 'Pacientes'];

    function MedicacaoDetailController($scope, $rootScope, $stateParams, entity, Medicacao, Pacientes) {
        var vm = this;
        vm.medicacao = entity;
        
        var unsubscribe = $rootScope.$on('safhApp:medicacaoUpdate', function(event, result) {
            vm.medicacao = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
