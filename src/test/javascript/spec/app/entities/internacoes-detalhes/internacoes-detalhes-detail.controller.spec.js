'use strict';

describe('Controller Tests', function() {

    describe('InternacoesDetalhes Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInternacoesDetalhes, MockInternacoes, MockDietas, MockPrescricoes, MockPosologias;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInternacoesDetalhes = jasmine.createSpy('MockInternacoesDetalhes');
            MockInternacoes = jasmine.createSpy('MockInternacoes');
            MockDietas = jasmine.createSpy('MockDietas');
            MockPrescricoes = jasmine.createSpy('MockPrescricoes');
            MockPosologias = jasmine.createSpy('MockPosologias');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InternacoesDetalhes': MockInternacoesDetalhes,
                'Internacoes': MockInternacoes,
                'Dietas': MockDietas,
                'Prescricoes': MockPrescricoes,
                'Posologias': MockPosologias
            };
            createController = function() {
                $injector.get('$controller')("InternacoesDetalhesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:internacoesDetalhesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
