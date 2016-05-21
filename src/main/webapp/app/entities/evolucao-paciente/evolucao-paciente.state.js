(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('evolucao-paciente', {
            parent: 'entity',
            url: '/evolucao-paciente',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.evolucaoPaciente.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/evolucao-paciente/evolucao-pacientes.html',
                    controller: 'EvolucaoPacienteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('evolucaoPaciente');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('evolucao-paciente-detail', {
            parent: 'entity',
            url: '/evolucao-paciente/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.evolucaoPaciente.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/evolucao-paciente/evolucao-paciente-detail.html',
                    controller: 'EvolucaoPacienteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('evolucaoPaciente');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EvolucaoPaciente', function($stateParams, EvolucaoPaciente) {
                    return EvolucaoPaciente.get({id : $stateParams.id});
                }]
            }
        })
        .state('evolucao-paciente.new', {
            parent: 'evolucao-paciente',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evolucao-paciente/evolucao-paciente-dialog.html',
                    controller: 'EvolucaoPacienteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dataEvolucao: null,
                                descricao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('evolucao-paciente', null, { reload: true });
                }, function() {
                    $state.go('evolucao-paciente');
                });
            }]
        })
        .state('evolucao-paciente.edit', {
            parent: 'evolucao-paciente',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evolucao-paciente/evolucao-paciente-dialog.html',
                    controller: 'EvolucaoPacienteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EvolucaoPaciente', function(EvolucaoPaciente) {
                            return EvolucaoPaciente.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('evolucao-paciente', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('evolucao-paciente.delete', {
            parent: 'evolucao-paciente',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evolucao-paciente/evolucao-paciente-delete-dialog.html',
                    controller: 'EvolucaoPacienteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EvolucaoPaciente', function(EvolucaoPaciente) {
                            return EvolucaoPaciente.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('evolucao-paciente', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
