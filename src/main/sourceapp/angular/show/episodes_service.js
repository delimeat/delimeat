angular
	.module('delimeat.show')
	.factory('EpisodesService', EpisodesService);
	
EpisodesService.$inject = ['$resource'];

function EpisodesService($resource) {

	var vm = this;
	
	var urlBase = 'api/info';
	
    vm._resource = $resource('api/info/:id', {}, {
    	read: { method: 'GET' }
    });
    
    return {
    	read: read
    };

    function read(id) {
    	return vm._resource.read({id:id}).$promise;
    }

}