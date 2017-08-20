angular
	.module('delimeat.episodes')
	.controller('EpisodesController', EpisodesController);

EpisodesController.$inject = ['$log','episodes'];

function EpisodesController($log,episodes) {
	
	var vm = this;
	
	vm._log = $log;

	vm.update = _update;
	
	// data
	vm.episodes = [];
	vm.title_filter = "";
	
	_onLoad();
	function _onLoad(){
		if(angular.isArray(episodes)){
			episodes.forEach(function(episode){
				episode.airDateTime = moment.tz(episode.airDate + ' ' + episode.show.airTime, episode.show.timezone).format();
			});
		}
		vm.episodes = episodes;
	}
	
	function _update(episode, status){
		vm._log.debug('START - CONTROLLER - EPISODE - update');
		episode.status = status;
		episode.$update({id:episode.episodeId}).then(function(){
			vm._log.debug('updated episode');
		});
		vm._log.debug('END - CONTROLLER - EPISODE - update');
	}

}