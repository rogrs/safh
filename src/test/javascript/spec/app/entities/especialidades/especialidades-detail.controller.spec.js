'use strict';

describe('Controller Tests', function() {

    describe('Especialidades Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEspecialidades;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEspecialidades = jasmine.createSpy('MockEspecialidades');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Especialidades': MockEspecialidades
            };
            createController = function() {
                $injector.get('$controller')("EspecialidadesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:especialidadesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
