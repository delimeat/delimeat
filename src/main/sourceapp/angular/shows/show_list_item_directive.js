angular
	.module('delimeat.shows')
	.controller('ShowListItemController',ShowListItemController);

ShowListItemController.$inject = [];

function ShowListItemController() {
	
	var vm = this;

	vm.formatDate = formatDate;
	
	function formatDate(date) {
		return Date.parse(date);
	}
	
}

angular
.module('delimeat.shows')
.directive('deliShowListItem',ShowListItemDirective);

function ShowListItemDirective($log) {
    var directive = {
            restrict: 'EA',
            templateUrl: 'app/shows/show_list_item.tmpl.html',
            scope: {
                show: '='
            },
            controller: ShowListItemController,
            controllerAs: 'vm',
            bindToController: true,
            transclude:true
        };
	
	return directive;
}