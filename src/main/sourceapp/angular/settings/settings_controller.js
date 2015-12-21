angular
	.module('delimeat.settings')
	.controller('SettingsController' , SettingsController);

SettingsController.$inject = ['$scope','$log','$location', 'SettingsModel','NotificationsService','DialogService'];

function SettingsController ($scope, $log, $location, SettingsModel,NotificationsService, DialogService) {
		
	var vm = this;
	
	vm._scope = $scope;
	vm._log = $log;
	vm._location = $location;
	
	vm._notificationsService = NotificationsService;
	vm._settingsModel = SettingsModel;
	vm._dialogService = DialogService;
	
	vm.settings = {};

	vm.leave = leave;
	
	_onLoad();
	function _onLoad(){
		vm._log.debug('START - CONTROLLER - SETTINGS - _onLoad');	
        vm._scope.$on('$destroy', _destroy);
		vm._notificationsService.add("settings.read.success",_loadedSettings);
		vm._notificationsService.add("settings.update.success",_returnHome);
		vm._settingsModel.read();
		vm._log.debug('END - CONTROLLER - SETTINGS - _onLoad');	
	}
	
	function _destroy(){
		vm._log.info('START - CONTROLLER - SHOW - _destroy');
		vm._notificationsService.add("settings.read.success",_loadedSettings);
		vm._notificationsService.add("settings.update.success",_returnHome);
		vm._log.info('END - CONTROLLER - SHOW - _destroy');
	}
	
	function _loadedSettings(){
		vm._log.debug('START - CONTROLLER - SETTINGS - _loaded');	
		vm.settings = vm._settingsModel.get();
		vm._log.debug(vm.data);
		vm._log.debug('END - CONTROLLER - SETTINGS - _loaded');	
	}

	function _returnHome(){
		vm._log.debug('START - CONTROLLER - SETTINGS - _returnHome');	
		vm._location.path("/");
		vm._log.debug('END - CONTROLLER - SETTINGS - _returnHome');	
	}
	
	function leave(){
		if(!vm._scope.form.$valid){
			vm._dialogService.confirm(undefined,'Leave without making changes?','Yes','No')
			.then(function(){
					_returnHome();
				}
			);			
		}
		else if(vm._scope.form.$dirty){
			vm._dialogService.confirm(undefined,'Do you want to save changes made?','Yes','No')
				.then(function(){
					vm._settingsModel.update(vm.settings);
				},function(){
					_returnHome();
				}
			);
		}
		else {
			_returnHome();
		}
	}

}
