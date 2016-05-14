(function() {
    'use strict';

    angular
        .module('safhApp')
        .controller('EnfermariasController', EnfermariasController);

    EnfermariasController.$inject = ['$scope', '$state', 'Enfermarias', 'EnfermariasSearch', 'ParseLinks', 'AlertService'];

    function EnfermariasController ($scope, $state, Enfermarias, EnfermariasSearch, ParseLinks, AlertService) {
        var vm = this;
        vm.enfermarias = [];
        vm.predicate = 'id';
        vm.reverse = true;
        vm.page = 0;
        vm.loadAll = function() {
            if (vm.currentSearch) {
                EnfermariasSearch.query({
                    query: vm.currentSearch,
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Enfermarias.query({
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
                    vm.enfermarias.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        };
        vm.reset = function() {
            vm.page = 0;
            vm.enfermarias = [];
            vm.loadAll();
        };
        vm.loadPage = function(page) {
            vm.page = page;
            vm.loadAll();
        };

        vm.search = function (searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.enfermarias = [];
            vm.links = null;
            vm.page = 0;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.loadAll();
        };

        vm.clear = function () {
            vm.enfermarias = [];
            vm.links = null;
            vm.page = 0;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.searchQuery = null;
            vm.currentSearch = null;
            vm.loadAll();
        };

        vm.loadAll();

    }
})();
