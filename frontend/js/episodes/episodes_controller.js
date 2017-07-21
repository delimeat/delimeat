angular
	.module('delimeat.episodes')
	.controller('EpisodesController', EpisodesController);

EpisodesController.$inject = ['$filter', /*'EpisodeService',*/'episodes'];

function EpisodesController($filter, /*EpisodeService,*/ episodes) {
	
	var vm = this;
	
	// data
	vm.episodes = [];
	
	_onLoad();
	function _onLoad(){
		if(angular.isArray(episodes)){
			episodes.forEach(function(episode){
				episode.airDateTime = new Date($filter('applytz')(episode.airDate + ' ' + episode.show.airTime, episode.show.timezone));
			});
		}
		vm.episodes = episodes;
	}

}