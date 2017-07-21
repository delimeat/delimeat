angular
	.module('delimeat.guide')
	.controller('GuideInfoController', GuideInfoController);

GuideInfoController.$inject = ['$log', '$location', 'ShowsService', 'info'];

function GuideInfoController($log, $location, ShowsService, info) {
	
	var vm = this;
	
	vm.angular = angular;
	vm._log = $log;
	vm._location = $location;
	
	vm._showsService = ShowsService;

	/*functions*/
	vm.create = create;
	
	/*variables*/
	vm.info = info;
	
	function create(){
		vm._log.info('START - CONTROLLER - GUIDE INFO - addShow');
		var show = {
				guideId: vm.info.guideId,
				airTime: vm.info.airTime,
				timezone: vm.info.timezone,
				title: vm.info.title,
				airing: vm.info.airing,
				showType: "SEASON",
				lastGuideUpdate: null,
				lastFeedUpdate: null,
				enabled: true,
				includeSpecials: false,
				minSize: (vm.angular.isNumber(vm.info.runningTime) ? vm.info.runningTime * 3 : 70),
				maxSize: (vm.angular.isNumber(vm.info.runningTime) ? vm.info.runningTime * 15 : 750)
				};
		
		if(vm.angular.isArray(vm.info.airDays) && vm.info.airDays.length > 1){
			show.showType="DAILY";
		}
		
		vm._showsService.create({},show).$promise.then(function(show){
			console.log(show);
			vm._location.path( "/show/"+show.showId );
		});
		vm._log.info('END - CONTROLLER - GUIDE INFO - addShow');
	}

}