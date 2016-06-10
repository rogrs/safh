(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('medicamentos', {
            parent: 'entity',
            url: '/medicamentos',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.medicamentos.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medicamentos/medicamentos.html',
                    controller: 'MedicamentosController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('medicamentos');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('medicamentos-detail', {
            parent: 'entity',
            url: '/medicamentos/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.medicamentos.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medicamentos/medicamentos-detail.html',
                    controller: 'MedicamentosDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('medicamentos');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Medicamentos', function($stateParams, Medicamentos) {
                    return Medicamentos.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('medicamentos.new', {
            parent: 'medicamentos',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicamentos/medicamentos-dialog.html',
                    controller: 'MedicamentosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                descricao: null,
                                registroMinisterioSaude: null,
                                codigoBarras: null,
                                qtdAtual: null,
                                qtdMin: null,
                                qtdMax: null,
                                observacoes: null,
                                apresentacao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('medicamentos', null, { reload: true });
                }, function() {
                    $state.go('medicamentos');
                });
            }]
        })
        .state('medicamentos.edit', {
            parent: 'medicamentos',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicamentos/medicamentos-dialog.html',
                    controller: 'MedicamentosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Medicamentos', function(Medicamentos) {
                            return Medicamentos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medicamentos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medicamentos.delete', {
            parent: 'medicamentos',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicamentos/medicamentos-delete-dialog.html',
                    controller: 'MedicamentosDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Medicamentos', function(Medicamentos) {
                            return Medicamentos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medicamentos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
