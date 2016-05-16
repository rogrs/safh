(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fabricantes', {
            parent: 'entity',
            url: '/fabricantes',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.fabricantes.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fabricantes/fabricantes.html',
                    controller: 'FabricantesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fabricantes');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('fabricantes-detail', {
            parent: 'entity',
            url: '/fabricantes/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.fabricantes.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fabricantes/fabricantes-detail.html',
                    controller: 'FabricantesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fabricantes');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Fabricantes', function($stateParams, Fabricantes) {
                    return Fabricantes.get({id : $stateParams.id});
                }]
            }
        })
        .state('fabricantes.new', {
            parent: 'fabricantes',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fabricantes/fabricantes-dialog.html',
                    controller: 'FabricantesDialogController',
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
                    $state.go('fabricantes', null, { reload: true });
                }, function() {
                    $state.go('fabricantes');
                });
            }]
        })
        .state('fabricantes.edit', {
            parent: 'fabricantes',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fabricantes/fabricantes-dialog.html',
                    controller: 'FabricantesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Fabricantes', function(Fabricantes) {
                            return Fabricantes.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('fabricantes', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fabricantes.delete', {
            parent: 'fabricantes',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fabricantes/fabricantes-delete-dialog.html',
                    controller: 'FabricantesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Fabricantes', function(Fabricantes) {
                            return Fabricantes.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('fabricantes', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
