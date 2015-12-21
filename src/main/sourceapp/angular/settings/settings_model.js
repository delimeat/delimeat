angular
	.module('delimeat.settings')
	.factory('SettingsModel', SettingsModel);

SettingsModel.$inject = ['$log','NotificationsService','SettingsService','AlertService'];

function SettingsModel($log,NotificationsService,SettingsService,AlertService) {
    
	var vm = this;
	
	vm._log = $log;
	
	vm._notificationsService = NotificationsService;
	vm._settingsService = SettingsService;
	vm._alertService = AlertService;
	
	vm._settings = null;
    
    return {
        read: read,
        update:update,
        get:get
    };
    
    function get(){
    	return vm._settings;
    }
    
	function read(){
		vm._log.debug('START - MODEL - SETTINGS - read');
		vm._notificationsService.notify("settings.read.start");
		vm._show = null;
		vm._settingsService.read()
			.then(function(response){
				vm._log.debug('SUCCESS - MODEL - SETTINGS - read');
				vm._settings = response;		
				vm._notificationsService.notify("settings.read.success");								
			},
			function(response){
				vm._log.debug('ERROR - MODEL - SETTINGS - read');
				vm._alertService.addError('Opps Something went wrong',5000);
				vm._notificationsService.notify("settings.read.error");								
			})
			.finally(function(){
				vm._log.debug('FINALLY - MODEL - SETTINGS - read');
				vm._notificationsService.notify("settings.read.end");								
			});
		vm._log.debug('END - MODEL - SETTINGS - read');

	}
	
	function update(settings){
		vm._log.debug('START - MODEL - SETTINGS - update');
		vm._notificationsService.notify("settings.update.start");
		vm._settingsService.update(settings)
			.then(function(response){
				vm._log.debug('SUCCESS - MODEL - SETTINGS - update');
				vm._alertService.addSuccess('Settings Updated',5000);
				vm._settings = settings;
				vm._notificationsService.notify("settings.update.success");								
			},
			function(response){
				vm._log.debug('ERROR - MODEL - SETTINGS - update');
				vm._alertService.addError('Opps Something went wrong',5000);
				vm._notificationsService.notify("settings.update.error");								
			})
		    .finally(function(){
				vm._log.debug('FINALLY - MODEL - SETTINGS - update');
				vm._notificationsService.notify("settings.update.end");								
		    });
		vm._log.debug('END - MODEL - SETTINGS - update');
	}
}