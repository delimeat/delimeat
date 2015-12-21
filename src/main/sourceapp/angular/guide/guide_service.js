angular
	.module('delimeat.guide')
	.factory('GuideService', GuideService);
	
GuideService.$inject = ['$resource'];

function GuideService($resource) {
	
	var vm = this;
	var urlBase = 'api/guide/search';
	
    vm._resource = $resource('api/guide/search/:title', {}, {
    	read: { method: 'GET', isArray: true }
    });
    
    return {
        read: read
    };

    
    function read(title) {
    	return vm._resource.read({title:title}).$promise;
    }
}