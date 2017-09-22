angular
	.module('delimeat.episodes')
	.factory('EpisodeService', EpisodeService);

EpisodeService.$inject = ['$resource','ResponseLoggingService','API'];

function EpisodeService($resource, ResponseLoggingService, API) {

	var vm = this;
	
	vm._logger = ResponseLoggingService;
	
	return $resource(API+'episode', {},{
    	query: { method: 'GET', isArray: true , interceptor:{responseError: vm._logger.error} , transformResponse: responseTransform},
        update: { method: 'PUT', url:API+'episode/:id', interceptor:{responseError: vm._logger.error} }
    });
	
    function responseTransform(data){
        var newData = angular.fromJson(data);
		if(angular.isArray(newData)){
			newData.forEach(function(episode){
				episode.airDateTime = moment.tz(episode.airDate + ' ' + episode.show.airTime, episode.show.timezone).format();
			});
		}
        return newData;
    }
}