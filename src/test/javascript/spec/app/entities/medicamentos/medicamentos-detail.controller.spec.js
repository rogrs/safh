'use strict';

describe('Controller Tests', function() {

    describe('Medicamentos Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMedicamentos, MockFabricantes, MockPosologia;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMedicamentos = jasmine.createSpy('MockMedicamentos');
            MockFabricantes = jasmine.createSpy('MockFabricantes');
            MockPosologia = jasmine.createSpy('MockPosologia');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Medicamentos': MockMedicamentos,
                'Fabricantes': MockFabricantes,
                'Posologia': MockPosologia
            };
            createController = function() {
                $injector.get('$controller')("MedicamentosDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:medicamentosUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
