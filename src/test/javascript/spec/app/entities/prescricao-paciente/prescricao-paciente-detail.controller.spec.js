'use strict';

describe('Controller Tests', function() {

    describe('PrescricaoPaciente Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPrescricaoPaciente, MockPacientes, MockMedicamentos, MockPosologias, MockDietas;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPrescricaoPaciente = jasmine.createSpy('MockPrescricaoPaciente');
            MockPacientes = jasmine.createSpy('MockPacientes');
            MockMedicamentos = jasmine.createSpy('MockMedicamentos');
            MockPosologias = jasmine.createSpy('MockPosologias');
            MockDietas = jasmine.createSpy('MockDietas');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PrescricaoPaciente': MockPrescricaoPaciente,
                'Pacientes': MockPacientes,
                'Medicamentos': MockMedicamentos,
                'Posologias': MockPosologias,
                'Dietas': MockDietas
            };
            createController = function() {
                $injector.get('$controller')("PrescricaoPacienteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:prescricaoPacienteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
