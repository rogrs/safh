(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('InternacoesDetalhesController', InternacoesDetalhesController);

    InternacoesDetalhesController.$inject = ['$scope', '$state', 'InternacoesDetalhes', 'InternacoesDetalhesSearch', 'ParseLinks', 'AlertService'];

    function InternacoesDetalhesController ($scope, $state, InternacoesDetalhes, InternacoesDetalhesSearch, ParseLinks, AlertService) {
        var vm = this;
        
        vm.internacoesDetalhes = [];
        vm.loadPage = loadPage;
        vm.page = 0;
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.clear = clear;
        vm.search = search;

        loadAll();

        function loadAll () {
            if (vm.currentSearch) {
                InternacoesDetalhesSearch.query({
                    query: vm.currentSearch,
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                InternacoesDetalhes.query({
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.internacoesDetalhes.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.internacoesDetalhes = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }

        function clear () {
            vm.internacoesDetalhes = [];
            vm.links = null;
            vm.page = 0;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.searchQuery = null;
            vm.currentSearch = null;
            vm.loadAll();
        }

        function search (searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.internacoesDetalhes = [];
            vm.links = null;
            vm.page = 0;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.loadAll();
        }
    }
})();
