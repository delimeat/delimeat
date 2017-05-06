angular
	.module('delimeat.guide')
	.factory('GuideService', GuideService);
	
GuideService.$inject = ['$resource','ResponseLoggingService','API'];

function GuideService( $resource, ResponseLoggingService, API) {

	var vm = this;
	
	vm._logger = ResponseLoggingService;
	
	return $resource(API+'guide', {}, {
    	info: { method: 'GET', url: API+'guide/info/:id', interceptor:{responseError: vm._logger.error} },
    	episodes: { method: 'GET', url: API+'guide/info/:id/episodes', isArray: true, interceptor:{responseError: vm._logger.error} },
    	search: { method: 'GET', url: API+'guide/search/:title', isArray: true, interceptor:{responseError: vm._logger.error} }
    });
    
}