angular
	.module('delimeat.settings')
	.factory('SettingsService', SettingsService);
	
SettingsService.$inject = ['$resource','ResponseLoggingService', 'API'];

function SettingsService( $resource, ResponseLoggingService, API) {

	var vm = this;
	
	vm._logger = ResponseLoggingService;
		
	return $resource(API+'config', {}, {
    	read: { method: 'GET', interceptor:{responseError: vm._logger.error} },
        update: { method: 'PUT', interceptor:{responseError: vm._logger.error} }
    });
    
}