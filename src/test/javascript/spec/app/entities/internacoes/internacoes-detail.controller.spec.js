'use strict';

describe('Controller Tests', function() {

    describe('Internacoes Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInternacoes, MockInternacoesDetalhes, MockPacientes, MockClinicas, MockMedicos;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInternacoes = jasmine.createSpy('MockInternacoes');
            MockInternacoesDetalhes = jasmine.createSpy('MockInternacoesDetalhes');
            MockPacientes = jasmine.createSpy('MockPacientes');
            MockClinicas = jasmine.createSpy('MockClinicas');
            MockMedicos = jasmine.createSpy('MockMedicos');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Internacoes': MockInternacoes,
                'InternacoesDetalhes': MockInternacoesDetalhes,
                'Pacientes': MockPacientes,
                'Clinicas': MockClinicas,
                'Medicos': MockMedicos
            };
            createController = function() {
                $injector.get('$controller')("InternacoesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:internacoesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
