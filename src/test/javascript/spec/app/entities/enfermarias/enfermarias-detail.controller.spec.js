'use strict';

describe('Controller Tests', function() {

    describe('Enfermarias Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEnfermarias, MockPacientes;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEnfermarias = jasmine.createSpy('MockEnfermarias');
            MockPacientes = jasmine.createSpy('MockPacientes');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Enfermarias': MockEnfermarias,
                'Pacientes': MockPacientes
            };
            createController = function() {
                $injector.get('$controller')("EnfermariasDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:enfermariasUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
