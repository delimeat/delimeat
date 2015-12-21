angular
	.module('delimeat.shows')
	.controller('ShowsController',ShowsController);

ShowsController.$inject = ['$scope','$log','ShowsModel','NotificationsService'];

function ShowsController($scope,$log,ShowsModel,NotificationsService) {
	
	var vm = this;
	
	vm._scope = $scope;
	vm._log = $log;
	
	vm._notificationsService = NotificationsService;
	vm._showsModel = ShowsModel;
	
	vm.shows = {};
	
	_onLoad();
	function _onLoad(){
		vm._log.info('START - CONTROLLER - SHOWS - _onLoad');
        vm._scope.$on('$destroy', _destroy);
		vm._notificationsService.add("shows.fetch.end",_loadedShows);
		vm._showsModel.read();
		vm._log.info('END - CONTROLLER - SHOWS - _onLoad');
	}
	
	function _destroy(){
		vm._log.info('START - CONTROLLER - SHOWS -_destroy');
		vm._notificationsService.remove("shows.fetch.end",_loadedShows);
		vm._log.info('END - CONTROLLER - SHOWS - _destroy');
	}
	
	function _loadedShows(){
		vm._log.debug("START - CONTROLLER - SHOWS - _showsLoaded");	
		vm.shows = vm._showsModel.get();
		vm._log.debug("END - CONTROLLER - SHOWS - _showsLoaded");	
	}
}