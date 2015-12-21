angular
	.module('delimeat.guide')
	.factory('GuideShowService', GuideShowService);

GuideShowService.$inject = ['$resource'];

function GuideShowService($resource) {

	var vm = this;
	var urlBase = 'api/shows/';
	
    vm._resource = $resource('api/shows/', {}, {
        create: {method:'POST'}
    });

    return {
        create: create
    };
    
    function create(guideid) {
    	return vm._resource.create({},guideid ).$promise;
    }
}