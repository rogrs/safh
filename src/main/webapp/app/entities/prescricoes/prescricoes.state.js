(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('prescricoes', {
            parent: 'entity',
            url: '/prescricoes',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.prescricoes.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prescricoes/prescricoes.html',
                    controller: 'PrescricoesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prescricoes');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('prescricoes-detail', {
            parent: 'entity',
            url: '/prescricoes/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.prescricoes.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prescricoes/prescricoes-detail.html',
                    controller: 'PrescricoesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prescricoes');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Prescricoes', function($stateParams, Prescricoes) {
                    return Prescricoes.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('prescricoes.new', {
            parent: 'prescricoes',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescricoes/prescricoes-dialog.html',
                    controller: 'PrescricoesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                prescricao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('prescricoes', null, { reload: true });
                }, function() {
                    $state.go('prescricoes');
                });
            }]
        })
        .state('prescricoes.edit', {
            parent: 'prescricoes',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescricoes/prescricoes-dialog.html',
                    controller: 'PrescricoesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prescricoes', function(Prescricoes) {
                            return Prescricoes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prescricoes', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prescricoes.delete', {
            parent: 'prescricoes',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescricoes/prescricoes-delete-dialog.html',
                    controller: 'PrescricoesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Prescricoes', function(Prescricoes) {
                            return Prescricoes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prescricoes', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
