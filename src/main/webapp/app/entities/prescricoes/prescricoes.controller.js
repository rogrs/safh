(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('PrescricoesController', PrescricoesController);

    PrescricoesController.$inject = ['$scope', '$state', 'Prescricoes', 'PrescricoesSearch', 'ParseLinks', 'AlertService'];

    function PrescricoesController ($scope, $state, Prescricoes, PrescricoesSearch, ParseLinks, AlertService) {
        var vm = this;
        
        vm.prescricoes = [];
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
                PrescricoesSearch.query({
                    query: vm.currentSearch,
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Prescricoes.query({
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
                    vm.prescricoes.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.prescricoes = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }

        function clear () {
            vm.prescricoes = [];
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
            vm.prescricoes = [];
            vm.links = null;
            vm.page = 0;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.loadAll();
        }
    }
})();
