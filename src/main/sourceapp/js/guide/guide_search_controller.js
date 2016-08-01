angular
	.module('delimeat.guide')
	.controller('GuideSearchController', GuideSearchController);

GuideSearchController.$inject = ['$location','title','results'];

function GuideSearchController($location, title, results) {
	
	var vm = this;
	
	vm._location = $location;

	vm.title = title;
	vm.results = results;
	
	vm.search = search;
	
	function search(){
		$location.path('/guide/search').search('title', vm.title);
	}
	
	
}