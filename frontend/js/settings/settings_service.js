angular
	.module('delimeat.settings')
	.factory('SettingsService', SettingsService);
	
SettingsService.$inject = ['$resource','ResponseLoggingService', 'API'];

function SettingsService( $resource, ResponseLoggingService, API) {

	var vm = this;
	
	vm._logger = ResponseLoggingService;
		
	return $resource(API+'config', {}, {
    	read: { method: 'GET', interceptor:{responseError: vm._logger.error}, transformResponse: responseTransform  },
        update: { method: 'PUT', interceptor:{responseError: vm._logger.error}, transformRequest: requestTransform, transformResponse: responseTransform }
    });
    
    function responseTransform(data){
        var newData = angular.fromJson(data);
        newData.searchDelayMins = Math.round( (newData.searchDelay|0) / 60000 );
        newData.searchIntervalMins = Math.round( (newData.searchInterval|0) / 60000 );
        return newData;
    }
    
    function requestTransform(data){
        data.searchDelay = Math.round( (data.searchDelayMins|0) * 60000 );
        data.searchInterval = Math.round( (data.searchIntervalMins|0) * 60000 );
        return angular.toJson(data);        
    }
    
}