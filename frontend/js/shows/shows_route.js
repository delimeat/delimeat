angular
	.module('delimeat.shows')
	.config(ShowsRouteConfig);

ShowsRouteConfig.$inject = ['$routeProvider'];

function ShowsRouteConfig($routeProvider, ShowsService) {
	$routeProvider.when('/show', {
		templateUrl : 'js/shows/shows.tmpl.html',
		controller : 'ShowsController',
		controllerAs : 'vm',
		title : "shows.page_title",
		resolve : {
			ShowsService : 'ShowsService',
			shows : ['ShowsService' ,function(ShowsService) {
				return ShowsService.query().$promise;
			}]
		}
	}).when('/show/:id', {
		templateUrl : 'js/shows/show.tmpl.html',
		controller : 'ShowController',
		controllerAs : 'vm',
		resolve : {
			ShowsService : 'ShowsService',
			show : ['$route','ShowsService', function($route, ShowsService) {
				return ShowsService.read({
					id : $route.current.params.id
				}).$promise;
			}]
		},
		title: function(locals){
			return locals.show.title;
		}
	});
  }