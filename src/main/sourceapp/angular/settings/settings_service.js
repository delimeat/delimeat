angular
	.module('delimeat.settings')
	.factory('SettingsService', SettingsService);
	
SettingsService.$inject = ['$resource'];

function SettingsService($resource) {
	
	var vm = this;
	var urlBase = 'api/config';
	
    vm._resource = $resource('api/config', {}, {
    	read: { method: 'GET' },
        update: { method: 'PUT' }
    });
	
    return {
        read: read,
        update: update
    };

    function read() {
    	return vm._resource.read().$promise;
    }
    
    function update(settings){
    	return vm._resource.update(settings).$promise;
    }
}