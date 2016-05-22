(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('internacao-paciente', {
            parent: 'entity',
            url: '/internacao-paciente',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.internacaoPaciente.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/internacao-paciente/internacao-pacientes.html',
                    controller: 'InternacaoPacienteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('internacaoPaciente');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('internacao-paciente-detail', {
            parent: 'entity',
            url: '/internacao-paciente/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.internacaoPaciente.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/internacao-paciente/internacao-paciente-detail.html',
                    controller: 'InternacaoPacienteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('internacaoPaciente');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InternacaoPaciente', function($stateParams, InternacaoPaciente) {
                    return InternacaoPaciente.get({id : $stateParams.id});
                }]
            }
        })
        .state('internacao-paciente.new', {
            parent: 'internacao-paciente',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/internacao-paciente/internacao-paciente-dialog.html',
                    controller: 'InternacaoPacienteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dataInternacao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('internacao-paciente', null, { reload: true });
                }, function() {
                    $state.go('internacao-paciente');
                });
            }]
        })
        .state('internacao-paciente.edit', {
            parent: 'internacao-paciente',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/internacao-paciente/internacao-paciente-dialog.html',
                    controller: 'InternacaoPacienteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InternacaoPaciente', function(InternacaoPaciente) {
                            return InternacaoPaciente.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('internacao-paciente', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('internacao-paciente.delete', {
            parent: 'internacao-paciente',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/internacao-paciente/internacao-paciente-delete-dialog.html',
                    controller: 'InternacaoPacienteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InternacaoPaciente', function(InternacaoPaciente) {
                            return InternacaoPaciente.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('internacao-paciente', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
