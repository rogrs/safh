'use strict';

describe('Controller Tests', function() {

    describe('Dietas Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDietas, MockInternacoesDetalhes;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDietas = jasmine.createSpy('MockDietas');
            MockInternacoesDetalhes = jasmine.createSpy('MockInternacoesDetalhes');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Dietas': MockDietas,
                'InternacoesDetalhes': MockInternacoesDetalhes
            };
            createController = function() {
                $injector.get('$controller')("DietasDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:dietasUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
