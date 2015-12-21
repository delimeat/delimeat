angular
	.module('delimeat.shows')
	.factory('ShowsService', ShowsService);

ShowsService.$inject = ['$resource'];

function ShowsService($resource) {

	var vm = this;
	var urlBase = 'api/shows/';
	
    vm._resource = $resource('api/shows/', {}, {
        get: { method: 'GET', isArray: true }
    });

    return {
        get: get
    };
    
    function get() {
    	return vm._resource.query().$promise;
    }
}