'use strict';

describe('Controller Tests', function() {

    describe('Clinicas Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockClinicas, MockLeitos, MockEnfermarias;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockClinicas = jasmine.createSpy('MockClinicas');
            MockLeitos = jasmine.createSpy('MockLeitos');
            MockEnfermarias = jasmine.createSpy('MockEnfermarias');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Clinicas': MockClinicas,
                'Leitos': MockLeitos,
                'Enfermarias': MockEnfermarias
            };
            createController = function() {
                $injector.get('$controller')("ClinicasDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'safhApp:clinicasUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
