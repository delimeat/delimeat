angular
	.module('delimeat.guide')
	.controller('GuideSearchController', GuideSearchController);

GuideSearchController.$inject = ['$scope','$log','$location','GuideSearchModel','NotificationsService'];

function GuideSearchController($scope, $log, $location, GuideSearchModel, NotificationsService) {
	
	var vm = this;
	
	vm._scope = $scope;
	vm._log = $log;
	vm._location = $location;
	
	vm._guideSearchModel = GuideSearchModel;
	vm._notificationsService = NotificationsService;

	vm.title = "";
	vm.results = {};
	
	vm.search = search;	
	vm.add = add;
	
	_onLoad();
	function _onLoad(){
		vm._log.info("START - CONTROLLER - GUIDE SEARCH - _onLoad");
        vm._scope.$on('$destroy', _destroy);
		vm._notificationsService.add("search.read.success",_loadedSearch);
		vm._log.info("END - CONTROLLER - GUIDE SEARCH - _onLoad");
	}
	
	function _destroy(){
		vm._log.info('START - CONTROLLER - GUIDE SEARCH -_destroy');
		vm._notificationsService.remove("search.read.success",_loadedSearch);
		vm._log.info('END - CONTROLLER - GUIDE SEARCH - _destroy');
	}
	
	function _loadedSearch(){
		vm._log.info("START - CONTROLLER - GUIDE SEARCH - _loaded")
        vm.results = vm._guideSearchModel.get();
		vm._log.info("END - CONTROLLER - GUIDE SEARCH - _loaded")
	}
	
	function search() {
		vm._guideSearchModel.read(vm.title);
	}
}