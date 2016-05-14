(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('clinicas', {
            parent: 'entity',
            url: '/clinicas',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.clinicas.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/clinicas/clinicas.html',
                    controller: 'ClinicasController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clinicas');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('clinicas-detail', {
            parent: 'entity',
            url: '/clinicas/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.clinicas.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/clinicas/clinicas-detail.html',
                    controller: 'ClinicasDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clinicas');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Clinicas', function($stateParams, Clinicas) {
                    return Clinicas.get({id : $stateParams.id});
                }]
            }
        })
        .state('clinicas.new', {
            parent: 'clinicas',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/clinicas/clinicas-dialog.html',
                    controller: 'ClinicasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                clinica: null,
                                descricao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('clinicas', null, { reload: true });
                }, function() {
                    $state.go('clinicas');
                });
            }]
        })
        .state('clinicas.edit', {
            parent: 'clinicas',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/clinicas/clinicas-dialog.html',
                    controller: 'ClinicasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Clinicas', function(Clinicas) {
                            return Clinicas.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('clinicas', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('clinicas.delete', {
            parent: 'clinicas',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/clinicas/clinicas-delete-dialog.html',
                    controller: 'ClinicasDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Clinicas', function(Clinicas) {
                            return Clinicas.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('clinicas', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
