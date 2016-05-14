(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('especialidades', {
            parent: 'entity',
            url: '/especialidades',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.especialidades.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/especialidades/especialidades.html',
                    controller: 'EspecialidadesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('especialidades');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('especialidades-detail', {
            parent: 'entity',
            url: '/especialidades/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.especialidades.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/especialidades/especialidades-detail.html',
                    controller: 'EspecialidadesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('especialidades');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Especialidades', function($stateParams, Especialidades) {
                    return Especialidades.get({id : $stateParams.id});
                }]
            }
        })
        .state('especialidades.new', {
            parent: 'especialidades',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/especialidades/especialidades-dialog.html',
                    controller: 'EspecialidadesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                descricao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('especialidades', null, { reload: true });
                }, function() {
                    $state.go('especialidades');
                });
            }]
        })
        .state('especialidades.edit', {
            parent: 'especialidades',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/especialidades/especialidades-dialog.html',
                    controller: 'EspecialidadesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Especialidades', function(Especialidades) {
                            return Especialidades.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('especialidades', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('especialidades.delete', {
            parent: 'especialidades',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/especialidades/especialidades-delete-dialog.html',
                    controller: 'EspecialidadesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Especialidades', function(Especialidades) {
                            return Especialidades.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('especialidades', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
