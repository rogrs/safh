'use strict';

describe('Controller Tests', function() {

    describe('Pacientes Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPacientes, MockInternacoes, MockClinicas, MockEnfermarias, MockLeitos;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPacientes = jasmine.createSpy('MockPacientes');
            MockInternacoes = jasmine.createSpy('MockInternacoes');
            MockClinicas = jasmine.createSpy('MockClinicas');
            MockEnfermarias = jasmine.createSpy('MockEnfermarias');
            MockLeitos = jasmine.createSpy('MockLeitos');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Pacientes': MockPacientes,
                'Internacoes': MockInternacoes,
                'Clinicas': MockClinicas,
                'Enfermarias': MockEnfermarias,
                'Leitos': MockLeitos
            };
            createController = function() {
                $injector.get('$controller')("PacientesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:pacientesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
