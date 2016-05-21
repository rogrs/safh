'use strict';

describe('Controller Tests', function() {

    describe('EvolucaoPaciente Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEvolucaoPaciente, MockPacientes;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEvolucaoPaciente = jasmine.createSpy('MockEvolucaoPaciente');
            MockPacientes = jasmine.createSpy('MockPacientes');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EvolucaoPaciente': MockEvolucaoPaciente,
                'Pacientes': MockPacientes
            };
            createController = function() {
                $injector.get('$controller')("EvolucaoPacienteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:evolucaoPacienteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
