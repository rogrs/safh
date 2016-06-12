'use strict';

describe('Controller Tests', function() {

    describe('Leitos Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLeitos, MockPacientes;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLeitos = jasmine.createSpy('MockLeitos');
            MockPacientes = jasmine.createSpy('MockPacientes');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Leitos': MockLeitos,
                'Pacientes': MockPacientes
            };
            createController = function() {
                $injector.get('$controller')("LeitosDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:leitosUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
