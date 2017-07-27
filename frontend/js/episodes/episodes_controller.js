angular
	.module('delimeat.episodes')
	.controller('EpisodesController', EpisodesController);

EpisodesController.$inject = ['$log','episodes'];

function EpisodesController($log,episodes) {
	
	var vm = this;
	
	vm._log = $log;
	
	vm.skip = skip;
	vm.pending = pending;
	
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
	
	function _update(episode){
		vm._log.debug('START - CONTROLLER - EPISODE - update');
		episode.$update({id:episode.episodeId}).then(function(){
			vm._log.debug('updated episode');
		});
		vm._log.debug('END - CONTROLLER - EPISODE - update');
	}
	
	function skip(episode){
		episode.status = SKIPPED;
		_update(episode);
	}
	
	function pending(episode){
		episode.status = PENDING;
		_update(episode);
	}

}