(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('leitos', {
            parent: 'entity',
            url: '/leitos',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.leitos.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/leitos/leitos.html',
                    controller: 'LeitosController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('leitos');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('leitos-detail', {
            parent: 'entity',
            url: '/leitos/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.leitos.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/leitos/leitos-detail.html',
                    controller: 'LeitosDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('leitos');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Leitos', function($stateParams, Leitos) {
                    return Leitos.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('leitos.new', {
            parent: 'leitos',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/leitos/leitos-dialog.html',
                    controller: 'LeitosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                leito: null,
                                tipo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('leitos', null, { reload: true });
                }, function() {
                    $state.go('leitos');
                });
            }]
        })
        .state('leitos.edit', {
            parent: 'leitos',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/leitos/leitos-dialog.html',
                    controller: 'LeitosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Leitos', function(Leitos) {
                            return Leitos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('leitos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('leitos.delete', {
            parent: 'leitos',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/leitos/leitos-delete-dialog.html',
                    controller: 'LeitosDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Leitos', function(Leitos) {
                            return Leitos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('leitos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
