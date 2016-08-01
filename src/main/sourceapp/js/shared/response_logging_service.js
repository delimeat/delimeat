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
		error:_error,
		success:_success
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
	
	function _success(response){
		vm._log.debug('START - SERVICE - RESPONSE_LOGGING- _success');
		var resource = response.resource;
		vm._log.debug(angular.toJson(resource,true));
		vm._log.debug('END - SERVICE - RESPONSE_LOGGING - _success');
		return resource;
	}
}