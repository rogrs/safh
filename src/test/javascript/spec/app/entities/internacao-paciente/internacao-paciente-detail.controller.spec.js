'use strict';

describe('Controller Tests', function() {

    describe('InternacaoPaciente Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInternacaoPaciente, MockPacientes, MockMedicos, MockEvolucaoPaciente;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInternacaoPaciente = jasmine.createSpy('MockInternacaoPaciente');
            MockPacientes = jasmine.createSpy('MockPacientes');
            MockMedicos = jasmine.createSpy('MockMedicos');
            MockEvolucaoPaciente = jasmine.createSpy('MockEvolucaoPaciente');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InternacaoPaciente': MockInternacaoPaciente,
                'Pacientes': MockPacientes,
                'Medicos': MockMedicos,
                'EvolucaoPaciente': MockEvolucaoPaciente
            };
            createController = function() {
                $injector.get('$controller')("InternacaoPacienteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:internacaoPacienteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
