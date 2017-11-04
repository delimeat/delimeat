/* global angular */

angular
.module('delimeat.shared')
.directive('dmEpisodesList', EpisodesListDirective);

EpisodesListDirective.$inject = ['$filter','$log'];

function EpisodesListDirective($filter,$log) {
	
    return {
		restrict: 'E',
		scope: {
			episodes: '=',
			display: '=',
			next: '=',
			previous: '=',
			airtime: '=',
			timezone: '='
		},
		controller: "EpisodesListController",
		controllerAs: 'vm',
		bindToController: true,
		templateUrl: 'js/shared/episodes.tmpl.html'
    };
}

angular
.module('delimeat.shared')
.controller('EpisodesListController', EpisodesListController);

EpisodesListController.$inject = ['$scope', '$log'];

function EpisodesListController($scope, $log) {
	var vm = this;
    
	vm.angular = angular;
	vm._scope = $scope;
	vm._log = $log;
	
	vm.seasons = [];
	vm.currentSeason = 0;
		
	(function init() {
		vm._scope.$watch("vm.next", watchNext);
		vm._scope.$watch("vm.episodes", watchEpisodes);
	})();
	
	vm.setNextEpisode = setNextEpisode;
	
	function setNextEpisode(episode){
		vm._log.info("START - DIRECTIVE - EPISODES - setNextEpisode");
		vm._log.info(angular.toJson(episode,true));
		var nextIdx = vm.episodes.indexOf(episode);
		vm._log.info(nextIdx);
		
		vm.next = angular.isObject(vm.episodes[nextIdx]) ? vm.episodes[nextIdx] : null;
		vm.previous = angular.isObject(vm.episodes[nextIdx + 1]) ? vm.episodes[nextIdx + 1] : null;
		if(vm.next === null){
			vm.currentSeason = vm.seasons[vm.seasons.length-1];
		}
		else{
			vm.currentSeason = vm.next.seasonNum;
		}
		vm.display = false;
		vm._log.info("END - DIRECTIVE - EPISODES - setNextEpisode");	
	}
    		
	function watchEpisodes(episodes) {
		vm._log.info("START - DIRECTIVE - EPISODES - watchEpisodes");
		vm._log.info(angular.toJson(episodes,true));
    	vm.seasons = [];
    	if(angular.isArray(episodes)){
    		for(var i=0;i<episodes.length;i++){
    			var episode = episodes[i];
				if(vm.seasons.indexOf(episode.seasonNum) == -1){
					vm.seasons.push(episode.seasonNum);
				}
    		}
			
			vm.seasons.sort(function(a, b){return b-a;});
			vm._log.info("seasons:" + vm.seasons);
			vm._log.info("currentSeason:" + vm.currentSeason);
			if(vm.currentSeason === 0 && vm.seasons.length > 0 ){
				vm.currentSeason = vm.seasons[0];
			}
			vm._log.info("currentSeason:" + vm.currentSeason);

		}
		vm._log.info("END - DIRECTIVE - EPISODES - watchEpisodes");
      }
      

   function watchNext(episode) {
		vm._log.info("START - DIRECTIVE - EPISODES - watchNext");
		vm._log.info(angular.toJson(episode,true));
		if (angular.isObject(episode)) {
			vm.currentSeason = episode.seasonNum;
		}
		vm._log.info("END - DIRECTIVE - EPISODES - watchNext");

	}	
}
