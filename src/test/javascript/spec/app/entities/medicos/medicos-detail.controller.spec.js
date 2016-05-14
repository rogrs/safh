'use strict';

describe('Controller Tests', function() {

    describe('Medicos Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMedicos, MockEspecialidades;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMedicos = jasmine.createSpy('MockMedicos');
            MockEspecialidades = jasmine.createSpy('MockEspecialidades');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Medicos': MockMedicos,
                'Especialidades': MockEspecialidades
            };
            createController = function() {
                $injector.get('$controller')("MedicosDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:medicosUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
