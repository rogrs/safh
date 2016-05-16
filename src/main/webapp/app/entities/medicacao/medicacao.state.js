(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('medicacao', {
            parent: 'entity',
            url: '/medicacao',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.medicacao.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medicacao/medicacaos.html',
                    controller: 'MedicacaoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('medicacao');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('medicacao-detail', {
            parent: 'entity',
            url: '/medicacao/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.medicacao.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medicacao/medicacao-detail.html',
                    controller: 'MedicacaoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('medicacao');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Medicacao', function($stateParams, Medicacao) {
                    return Medicacao.get({id : $stateParams.id});
                }]
            }
        })
        .state('medicacao.new', {
            parent: 'medicacao',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicacao/medicacao-dialog.html',
                    controller: 'MedicacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dataMedicacao: null,
                                verificar: null,
                                valor: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('medicacao', null, { reload: true });
                }, function() {
                    $state.go('medicacao');
                });
            }]
        })
        .state('medicacao.edit', {
            parent: 'medicacao',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicacao/medicacao-dialog.html',
                    controller: 'MedicacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Medicacao', function(Medicacao) {
                            return Medicacao.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('medicacao', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medicacao.delete', {
            parent: 'medicacao',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicacao/medicacao-delete-dialog.html',
                    controller: 'MedicacaoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Medicacao', function(Medicacao) {
                            return Medicacao.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('medicacao', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
