angular
	.module('delimeat.shows')
	.factory('ShowsService', ShowsService);

ShowsService.$inject = ['$resource','ResponseLoggingService','API'];

function ShowsService($resource, ResponseLoggingService, API) {

	var vm = this;
	
	vm._logger = ResponseLoggingService;
	
	return $resource(API+'shows', {},{
    	query: { method: 'GET', isArray: true , interceptor:{responseError: vm._logger.error, response: vm._logger.success} },
    	read: { method: 'GET', url:API+'shows/:id', interceptor:{responseError: vm._logger.error, response: vm._logger.success} },
        update: { method: 'PUT', url:API+'shows/:id', interceptor:{responseError: vm._logger.error, response: vm._logger.success} },
        remove: { method: 'DELETE', url:API+'shows/:id', interceptor:{responseError: vm._logger.error, response: vm._logger.success} },
        create: {method:'POST', interceptor:{responseError: vm._logger.error, response: vm._logger.success} },
        episodes: { method: 'GET', url:API+'shows/:id/episodes', isArray:true, interceptor:{responseError: vm._logger.error, response: vm._logger.success} }
    });
	
}