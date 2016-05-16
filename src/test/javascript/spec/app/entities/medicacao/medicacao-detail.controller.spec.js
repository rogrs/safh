'use strict';

describe('Controller Tests', function() {

    describe('Medicacao Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMedicacao, MockPaciente;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMedicacao = jasmine.createSpy('MockMedicacao');
            MockPaciente = jasmine.createSpy('MockPaciente');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Medicacao': MockMedicacao,
                'Paciente': MockPaciente
            };
            createController = function() {
                $injector.get('$controller')("MedicacaoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:medicacaoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
