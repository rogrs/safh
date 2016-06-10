(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('internacoes', {
            parent: 'entity',
            url: '/internacoes',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.internacoes.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/internacoes/internacoes.html',
                    controller: 'InternacoesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('internacoes');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('internacoes-detail', {
            parent: 'entity',
            url: '/internacoes/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.internacoes.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/internacoes/internacoes-detail.html',
                    controller: 'InternacoesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('internacoes');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Internacoes', function($stateParams, Internacoes) {
                    return Internacoes.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('internacoes.new', {
            parent: 'internacoes',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/internacoes/internacoes-dialog.html',
                    controller: 'InternacoesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dataInternacao: null,
                                descricao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('internacoes', null, { reload: true });
                }, function() {
                    $state.go('internacoes');
                });
            }]
        })
        .state('internacoes.edit', {
            parent: 'internacoes',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/internacoes/internacoes-dialog.html',
                    controller: 'InternacoesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Internacoes', function(Internacoes) {
                            return Internacoes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('internacoes', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('internacoes.delete', {
            parent: 'internacoes',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/internacoes/internacoes-delete-dialog.html',
                    controller: 'InternacoesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Internacoes', function(Internacoes) {
                            return Internacoes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('internacoes', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
