(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('internacoes-detalhes', {
            parent: 'entity',
            url: '/internacoes-detalhes',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.internacoesDetalhes.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/internacoes-detalhes/internacoes-detalhes.html',
                    controller: 'InternacoesDetalhesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('internacoesDetalhes');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('internacoes-detalhes-detail', {
            parent: 'entity',
            url: '/internacoes-detalhes/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.internacoesDetalhes.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/internacoes-detalhes/internacoes-detalhes-detail.html',
                    controller: 'InternacoesDetalhesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('internacoesDetalhes');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InternacoesDetalhes', function($stateParams, InternacoesDetalhes) {
                    return InternacoesDetalhes.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('internacoes-detalhes.new', {
            parent: 'internacoes-detalhes',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/internacoes-detalhes/internacoes-detalhes-dialog.html',
                    controller: 'InternacoesDetalhesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dataDetalhe: null,
                                horario: null,
                                qtd: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('internacoes-detalhes', null, { reload: true });
                }, function() {
                    $state.go('internacoes-detalhes');
                });
            }]
        })
        .state('internacoes-detalhes.edit', {
            parent: 'internacoes-detalhes',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/internacoes-detalhes/internacoes-detalhes-dialog.html',
                    controller: 'InternacoesDetalhesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InternacoesDetalhes', function(InternacoesDetalhes) {
                            return InternacoesDetalhes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('internacoes-detalhes', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('internacoes-detalhes.delete', {
            parent: 'internacoes-detalhes',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/internacoes-detalhes/internacoes-detalhes-delete-dialog.html',
                    controller: 'InternacoesDetalhesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InternacoesDetalhes', function(InternacoesDetalhes) {
                            return InternacoesDetalhes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('internacoes-detalhes', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
