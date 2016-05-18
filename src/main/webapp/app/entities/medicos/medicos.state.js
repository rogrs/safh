(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('medicos', {
            parent: 'entity',
            url: '/medicos',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.medicos.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medicos/medicos.html',
                    controller: 'MedicosController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('medicos');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('medicos-detail', {
            parent: 'entity',
            url: '/medicos/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.medicos.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medicos/medicos-detail.html',
                    controller: 'MedicosDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('medicos');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Medicos', function($stateParams, Medicos) {
                    return Medicos.get({id : $stateParams.id});
                }]
            }
        })
        .state('medicos.new', {
            parent: 'medicos',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicos/medicos-dialog.html',
                    controller: 'MedicosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                crm: null,
                                cpf: null,
                                email: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('medicos', null, { reload: true });
                }, function() {
                    $state.go('medicos');
                });
            }]
        })
        .state('medicos.edit', {
            parent: 'medicos',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicos/medicos-dialog.html',
                    controller: 'MedicosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Medicos', function(Medicos) {
                            return Medicos.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('medicos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medicos.delete', {
            parent: 'medicos',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicos/medicos-delete-dialog.html',
                    controller: 'MedicosDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Medicos', function(Medicos) {
                            return Medicos.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('medicos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
