angular
.module('delimeat.shared')
.factory('ResponseLoggingService', ResponseLoggingService);

ResponseLoggingService.$inject = ['$log','$translate','AlertService'];

function ResponseLoggingService($log, $translate, AlertService) {
	
	var vm = this;
	
	vm._log = $log;
	vm._translate = $translate;
	
	vm._alertService = AlertService;
	
	return {
		error:_error
		};
	
	function _error(response){
		vm._log.debug('START - SERVICE - RESPONSE_LOGGING - _error');
		var resource = response.resource;
		vm._log.debug(angular.toJson(response,true));
		vm._translate("common.unexpected_error").then(function(message){
			vm._alertService.addError(message,5000);
		});
		vm._log.debug('END - SERVICE - RESPONSE_LOGGING - _error');
		return resource;
	}
}