angular
	.module('delimeat.shows')
	.factory('ShowsService', ShowsService);

ShowsService.$inject = ['$resource','ResponseLoggingService','API'];

function ShowsService($resource, ResponseLoggingService, API) {

	var vm = this;
	
	vm._logger = ResponseLoggingService;
	
	return $resource(API+'show', {},{
    	query: { method: 'GET', isArray: true , interceptor:{responseError: vm._logger.error} },
    	read: { method: 'GET', url:API+'show/:id', interceptor:{responseError: vm._logger.error} },
        update: { method: 'PUT', url:API+'show/:id', interceptor:{responseError: vm._logger.error} },
        remove: { method: 'DELETE', url:API+'show/:id', interceptor:{responseError: vm._logger.error} },
        create: {method:'POST', interceptor:{responseError: vm._logger.error} },
        episodes: { method: 'GET', url:API+'show/:id/episodes', isArray:true, interceptor:{responseError: vm._logger.error} }
    });
	
}