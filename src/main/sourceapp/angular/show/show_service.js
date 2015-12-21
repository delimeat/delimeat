angular
	.module('delimeat.show')
	.factory('ShowService', ShowService);

ShowService.$inject = ['$resource'];

function ShowService($resource) {

	var vm = this;
	var urlBase = 'api/shows/';

    vm._resource = $resource('api/shows/:id', {}, {
    	read: { method: 'GET' },
        update: { method: 'PUT' },
        remove: { method: 'DELETE' }
    });
	
    return {
    	read: read,
        update: update,
        remove: remove
    };
    
    function read(id) {
    	return vm._resource.read({id:id}).$promise;
    }
    
    function update(show){
    	return vm._resource.update({id:show.showId},show).$promise;
    }
    
    function remove(id){
    	return vm._resource.remove({id:id}).$promise;
    }
}