(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('prescricao-paciente', {
            parent: 'entity',
            url: '/prescricao-paciente',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.prescricaoPaciente.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prescricao-paciente/prescricao-pacientes.html',
                    controller: 'PrescricaoPacienteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prescricaoPaciente');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('prescricao-paciente-detail', {
            parent: 'entity',
            url: '/prescricao-paciente/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.prescricaoPaciente.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prescricao-paciente/prescricao-paciente-detail.html',
                    controller: 'PrescricaoPacienteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prescricaoPaciente');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PrescricaoPaciente', function($stateParams, PrescricaoPaciente) {
                    return PrescricaoPaciente.get({id : $stateParams.id});
                }]
            }
        })
        .state('prescricao-paciente.new', {
            parent: 'prescricao-paciente',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescricao-paciente/prescricao-paciente-dialog.html',
                    controller: 'PrescricaoPacienteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dataEntrada: null,
                                quant: null,
                                obse: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('prescricao-paciente', null, { reload: true });
                }, function() {
                    $state.go('prescricao-paciente');
                });
            }]
        })
        .state('prescricao-paciente.edit', {
            parent: 'prescricao-paciente',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescricao-paciente/prescricao-paciente-dialog.html',
                    controller: 'PrescricaoPacienteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PrescricaoPaciente', function(PrescricaoPaciente) {
                            return PrescricaoPaciente.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('prescricao-paciente', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prescricao-paciente.delete', {
            parent: 'prescricao-paciente',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescricao-paciente/prescricao-paciente-delete-dialog.html',
                    controller: 'PrescricaoPacienteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PrescricaoPaciente', function(PrescricaoPaciente) {
                            return PrescricaoPaciente.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('prescricao-paciente', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
