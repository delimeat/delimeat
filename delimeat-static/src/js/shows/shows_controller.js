angular
	.module('delimeat.shows')
	.controller('ShowsController', ShowsController);

ShowsController.$inject = ['shows'];

function ShowsController(shows) {
	
	var vm = this;
	
	// data
	vm.shows = shows;
	
	vm.title_filter = "";

}