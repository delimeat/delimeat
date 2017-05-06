angular
	.module('delimeat.guide')
	.controller('GuideInfoController', GuideInfoController);

GuideInfoController.$inject = ['$log', '$location', '$filter','$q', 'ShowsService', 'data'];

function GuideInfoController($log, $location, $filter,$q, ShowsService, data) {
	
	var vm = this;
	
	vm.angular = angular;
	vm._log = $log;
	vm._location = $location;
	
	vm._showsService = ShowsService;

	/*functions*/
	vm.create = create;
	
	/*variables*/
	vm.info = data.info;
	vm.episodes = data.episodes;
	vm.show = data.show;

	vm.displayEps = false;
	
	function create(){
		vm._log.info('START - CONTROLLER - GUIDE INFO - addShow');
		vm._showsService.create({},vm.show).$promise.then(function(show){
			console.log(show);
			vm._location.path( "/show/"+show.showId );
		});
		vm._log.info('END - CONTROLLER - GUIDE INFO - addShow');
	}

}