'use strict';

describe('Controller Tests', function() {

    describe('Prescricoes Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPrescricoes, MockInternacoesDetalhes;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPrescricoes = jasmine.createSpy('MockPrescricoes');
            MockInternacoesDetalhes = jasmine.createSpy('MockInternacoesDetalhes');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Prescricoes': MockPrescricoes,
                'InternacoesDetalhes': MockInternacoesDetalhes
            };
            createController = function() {
                $injector.get('$controller')("PrescricoesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:prescricoesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
