'use strict';

describe('Controller Tests', function() {

    describe('Fabricantes Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFabricantes, MockMedicamentos;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFabricantes = jasmine.createSpy('MockFabricantes');
            MockMedicamentos = jasmine.createSpy('MockMedicamentos');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Fabricantes': MockFabricantes,
                'Medicamentos': MockMedicamentos
            };
            createController = function() {
                $injector.get('$controller')("FabricantesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:fabricantesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
