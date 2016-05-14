(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('enfermarias', {
            parent: 'entity',
            url: '/enfermarias',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.enfermarias.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/enfermarias/enfermarias.html',
                    controller: 'EnfermariasController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('enfermarias');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('enfermarias-detail', {
            parent: 'entity',
            url: '/enfermarias/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.enfermarias.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/enfermarias/enfermarias-detail.html',
                    controller: 'EnfermariasDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('enfermarias');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Enfermarias', function($stateParams, Enfermarias) {
                    return Enfermarias.get({id : $stateParams.id});
                }]
            }
        })
        .state('enfermarias.new', {
            parent: 'enfermarias',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enfermarias/enfermarias-dialog.html',
                    controller: 'EnfermariasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                enfermaria: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('enfermarias', null, { reload: true });
                }, function() {
                    $state.go('enfermarias');
                });
            }]
        })
        .state('enfermarias.edit', {
            parent: 'enfermarias',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enfermarias/enfermarias-dialog.html',
                    controller: 'EnfermariasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Enfermarias', function(Enfermarias) {
                            return Enfermarias.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('enfermarias', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('enfermarias.delete', {
            parent: 'enfermarias',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/enfermarias/enfermarias-delete-dialog.html',
                    controller: 'EnfermariasDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Enfermarias', function(Enfermarias) {
                            return Enfermarias.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('enfermarias', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
