(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('posologias', {
            parent: 'entity',
            url: '/posologias',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.posologias.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/posologias/posologias.html',
                    controller: 'PosologiasController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('posologias');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('posologias-detail', {
            parent: 'entity',
            url: '/posologias/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.posologias.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/posologias/posologias-detail.html',
                    controller: 'PosologiasDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('posologias');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Posologias', function($stateParams, Posologias) {
                    return Posologias.get({id : $stateParams.id});
                }]
            }
        })
        .state('posologias.new', {
            parent: 'posologias',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/posologias/posologias-dialog.html',
                    controller: 'PosologiasDialogController',
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
                    $state.go('posologias', null, { reload: true });
                }, function() {
                    $state.go('posologias');
                });
            }]
        })
        .state('posologias.edit', {
            parent: 'posologias',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/posologias/posologias-dialog.html',
                    controller: 'PosologiasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Posologias', function(Posologias) {
                            return Posologias.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('posologias', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('posologias.delete', {
            parent: 'posologias',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/posologias/posologias-delete-dialog.html',
                    controller: 'PosologiasDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Posologias', function(Posologias) {
                            return Posologias.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('posologias', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
