angular
	.module('delimeat.shows')
	.controller('ShowsController', ShowsController);

ShowsController.$inject = ['$filter','shows'];

function ShowsController($filter,shows) {
	
	var vm = this;
	
	// data
	vm.shows = [];
	
	_onLoad();
	function _onLoad(){
		if(angular.isArray(shows) && shows.length > 0){
			for(var i=0;i<shows.length;i++){
				var show = shows[i];
				if(angular.isObject(show.nextEpisode)){
					var airDateTime = new Date($filter('applytz')(show.nextEpisode.airDate + ' ' + show.airTime, show.timezone));
					show.nextEpisode.airDateTime = airDateTime;
				}
			}
		}
		vm.shows = shows;
	}

}