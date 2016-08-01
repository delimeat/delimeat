angular
.module('delimeat.settings')
.config(SettingsRouteConfig);

SettingsRouteConfig.$inject = ['$routeProvider'];

function SettingsRouteConfig($routeProvider) {
    $routeProvider.
    	when('/settings', {
    	templateUrl: 'js/settings/settings.tmpl.html',
    	controller: 'SettingsController',
    	controllerAs: 'vm',
    	title: 'settings.page_title',
    	resolve: {
    		SettingsService: 'SettingsService',
    		settings: ['SettingsService', function(SettingsService){
    			return SettingsService.read().$promise;
    		}]
    	}
    });
 }