angular
	.module('delimeat.show')
	.controller('EpisodesModalController', EpisodesModalController);

EpisodesModalController.$inject = ['$scope','$mdDialog','$log','episodes','previousEpisode','nextEpisode'];

function EpisodesModalController($scope, $mdDialog,$log,episodes, previousEpisode,nextEpisode) {

	var vm = this;	
	
	vm._scope = $scope;
	vm._dialog = $mdDialog;
	vm._log = $log;
	
	vm._scope.episodes = episodes;
	vm.previousEpisode = previousEpisode;
	vm.nextEpisode = nextEpisode;

	vm._scope.cancel = cancel;
	vm._scope.isSelected = isSelected;
	vm._scope.ok = ok;
	
	function isSelected(episode) {
		if (vm.nextEpisode !== null && vm.nextEpisode.seasonNum === episode.seasonNum && vm.nextEpisode.episodeNum === episode.episodeNum) {
			return true;
		}
		return false;
	}

	function cancel() {
		vm._dialog.cancel();
	}
	
	function ok(episode) {
		vm._log.debug('START - CONTROLLER - EPISODES - ok');
		if(episode===null){
			vm.nextEpisode = null;
			vm.previousEpisode = vm._scope.episodes[0];
		}else{
			vm.nextEpisode = episode;
			var index = vm._scope.episodes.indexOf(episode);
			if(index < vm._scope.episodes.length){
				vm.previousEpisode = vm._scope.episodes[index+1];
			}else{
				vm.previousEpisode = null;
			}
		}
		vm._dialog.hide({nextEpisode:vm.nextEpisode,previousEpisode:vm.previousEpisode});
		vm._log.debug('END - CONTROLLER - EPISODES - ok');
	}
}