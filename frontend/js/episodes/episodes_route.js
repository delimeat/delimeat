angular
	.module('delimeat.episodes')
	.config(EpisodesRouteConfig);

EpisodesRouteConfig.$inject = ['$routeProvider'];

function EpisodesRouteConfig($routeProvider, EpisodeService) {
	$routeProvider.when('/', {
		templateUrl : 'js/episodes/episodes.tmpl.html',
		controller : 'EpisodesController',
		controllerAs : 'vm',
		title : "episodes.page_title",
		resolve : {
			EpisodeService : 'EpisodeService',
			episodes : ['EpisodeService' ,function(EpisodeService) {
				return EpisodeService.query().$promise;
			}],
			showFilter: function(){
				return true;
			},
			sortDesc: function(){
				return false;
			}
		}
	}).otherwise({
		redirectTo : '/'
	});
  }