var GuideModule = angular.module('delimeat.guide');

GuideModule.config(GuideRouteConfig);

GuideRouteConfig.$inject = ['$routeProvider'];

function GuideRouteConfig($routeProvider) {
    $routeProvider.
    	when('/guide/search', {
    		templateUrl: 'js/guide/guide_search.tmpl.html',
    		controller: 'GuideSearchController',
    		controllerAs: 'vm',
    		resolve: {
    			results: ['$route','GuideService', function($route, GuideService){
    				if(angular.isString($route.current.params.title)){
    					return GuideService.search({title:$route.current.params.title}).$promise;
    				}else{
    					return [];
    				}
    			}],
    			title: ['$route', function($route){
    				return $route.current.params.title;
    			}]
    		}
    	}).
    	when('/guide/info/:id', {
    		templateUrl: 'js/guide/guide_info.tmpl.html',
    		controller: 'GuideInfoController',
    		controllerAs: 'vm',
    		resolve: {
    			info: ['$route','GuideService', function($route, GuideService){
    		        return GuideService.info({id:$route.current.params.id}).$promise;
    			}]
    		}
    	});
 }
