'use strict';

describe('Controller Tests', function() {

    describe('Leitos Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLeitos;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLeitos = jasmine.createSpy('MockLeitos');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Leitos': MockLeitos
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
