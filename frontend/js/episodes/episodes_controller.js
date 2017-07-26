angular
	.module('delimeat.episodes')
	.controller('EpisodesController', EpisodesController);

EpisodesController.$inject = ['episodes'];

function EpisodesController(episodes) {
	
	var vm = this;
	
	// data
	vm.episodes = [];
	
	_onLoad();
	function _onLoad(){
		if(angular.isArray(episodes)){
			episodes.forEach(function(episode){
				episode.airDateTime = moment.tz(episode.airDate + ' ' + episode.show.airTime, episode.show.timezone).format();
			});
		}
		vm.episodes = episodes;
	}

}