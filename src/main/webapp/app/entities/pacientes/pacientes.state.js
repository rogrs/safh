(function() {
    'use strict';

    angular
        .module('safhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pacientes', {
            parent: 'entity',
            url: '/pacientes',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.pacientes.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pacientes/pacientes.html',
                    controller: 'PacientesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pacientes');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pacientes-detail', {
            parent: 'entity',
            url: '/pacientes/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'safhApp.pacientes.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pacientes/pacientes-detail.html',
                    controller: 'PacientesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pacientes');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pacientes', function($stateParams, Pacientes) {
                    return Pacientes.get({id : $stateParams.id});
                }]
            }
        })
        .state('pacientes.new', {
            parent: 'pacientes',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pacientes/pacientes-dialog.html',
                    controller: 'PacientesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                sobrenome: null,
                                naturalidade: null,
                                telefone: null,
                                celular: null,
                                peso: null,
                                observacao: null,
                                nascimento: null,
                                endereco: null,
                                numero: null,
                                complemento: null,
                                bairro: null,
                                cidade: null,
                                uf: null,
                                cpf: null,
                                email: null,
                                cep: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pacientes', null, { reload: true });
                }, function() {
                    $state.go('pacientes');
                });
            }]
        })
        .state('pacientes.edit', {
            parent: 'pacientes',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pacientes/pacientes-dialog.html',
                    controller: 'PacientesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pacientes', function(Pacientes) {
                            return Pacientes.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pacientes', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pacientes.delete', {
            parent: 'pacientes',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pacientes/pacientes-delete-dialog.html',
                    controller: 'PacientesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pacientes', function(Pacientes) {
                            return Pacientes.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pacientes', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
