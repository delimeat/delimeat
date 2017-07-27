angular
	.module('delimeat.episodes')
	.factory('EpisodeService', EpisodeService);

EpisodeService.$inject = ['$resource','ResponseLoggingService','API'];

function EpisodeService($resource, ResponseLoggingService, API) {

	var vm = this;
	
	vm._logger = ResponseLoggingService;
	
	return $resource(API+'episode', {},{
    	query: { method: 'GET', isArray: true , interceptor:{responseError: vm._logger.error} },
        update: { method: 'PUT', url:API+'episode/:id', interceptor:{responseError: vm._logger.error} }
    });
	
}