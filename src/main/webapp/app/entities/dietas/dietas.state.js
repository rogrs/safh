(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dietas', {
            parent: 'entity',
            url: '/dietas',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.dietas.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dietas/dietas.html',
                    controller: 'DietasController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dietas');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('dietas-detail', {
            parent: 'entity',
            url: '/dietas/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.dietas.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dietas/dietas-detail.html',
                    controller: 'DietasDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dietas');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Dietas', function($stateParams, Dietas) {
                    return Dietas.get({id : $stateParams.id});
                }]
            }
        })
        .state('dietas.new', {
            parent: 'dietas',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dietas/dietas-dialog.html',
                    controller: 'DietasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dieta: null,
                                descricao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dietas', null, { reload: true });
                }, function() {
                    $state.go('dietas');
                });
            }]
        })
        .state('dietas.edit', {
            parent: 'dietas',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dietas/dietas-dialog.html',
                    controller: 'DietasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dietas', function(Dietas) {
                            return Dietas.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('dietas', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dietas.delete', {
            parent: 'dietas',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dietas/dietas-delete-dialog.html',
                    controller: 'DietasDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Dietas', function(Dietas) {
                            return Dietas.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('dietas', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
