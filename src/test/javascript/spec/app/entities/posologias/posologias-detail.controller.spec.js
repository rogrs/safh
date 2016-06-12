'use strict';

describe('Controller Tests', function() {

    describe('Posologias Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPosologias, MockMedicamentos, MockInternacoesDetalhes;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPosologias = jasmine.createSpy('MockPosologias');
            MockMedicamentos = jasmine.createSpy('MockMedicamentos');
            MockInternacoesDetalhes = jasmine.createSpy('MockInternacoesDetalhes');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Posologias': MockPosologias,
                'Medicamentos': MockMedicamentos,
                'InternacoesDetalhes': MockInternacoesDetalhes
            };
            createController = function() {
                $injector.get('$controller')("PosologiasDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:posologiasUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
