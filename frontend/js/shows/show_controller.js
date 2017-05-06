angular
	.module('delimeat.shows')
	.controller('ShowController', ShowController);

ShowController.$inject = ['$scope','$log', '$location', '$filter', '$window','$translate','ShowsService', 'show'];

function ShowController($scope, $log, $location, $filter,$window,$translate, ShowsService, show) {

	var vm = this;	

	vm._scope = $scope;
	vm._log = $log;
	vm._location = $location;
	vm._filter = $filter;
	vm._window = $window;
	vm._translate = $translate;

	vm._showsService = ShowsService;

	vm.remove = remove;
	vm.update = update;
	
	vm._scope.$watch("vm.displayEps", _watchDisplayEps);
	vm._scope.$watch("vm.show.nextEpisode", _watchNextEpisode);

	vm.show = show;
	vm.episodes = [];

	vm.displayEps = false;
	
	function update(){
		vm._log.debug('START - CONTROLLER - SHOW - update');
		vm.show.$update({id:vm.show.showId}).then(function(){
			vm._location.path("/");
		});
		vm._log.debug('END - CONTROLLER - SHOW - update');
	}
	
	function remove(){
		vm._log.debug('START - CONTROLLER - SHOW - remove');
		vm._translate("show.confirm_remove").then(function (translation) {
        	if(vm._window.confirm(translation)){
	    		vm.show.$remove({id:vm.show.showId}).then(function(){
	    			vm._location.path("/");
	    		});
        	}

          });
		vm._log.debug('END - CONTROLLER - SHOW - remove');
	}
	
	function _watchDisplayEps(displayEps){
		vm._log.debug('START - CONTROLLER - SHOW - _watchDisplayEps');
		if(displayEps === true && vm.episodes.length === 0){
			vm._showsService.episodes({id:vm.show.showId}).$promise.then(_loadedEpisodes);	
		}
		vm._log.debug('END - CONTROLLER - SHOW - _watchDisplayEps');			
	}
	
	function _loadedEpisodes(episodes){
		vm._log.info('START - CONTROLLER - SHOW - _loadedEpisodes');
		vm._log.debug(angular.toJson(episodes, true));
		vm.episodes = vm._filter('orderBy')(episodes, ['seasonNum','episodeNum','airDate'], true);
		vm._log.info('END - CONTROLLER - SHOW - _loadedEpisodes');
	}
	
	function _watchNextEpisode(newEpisode, oldEpisode){
		vm._log.debug('START - CONTROLLER - SHOW - _watchNextEpisode');
		if(angular.equals(newEpisode, oldEpisode) !== true){
			vm.form.$setDirty();			
		}
		vm._log.debug('END - CONTROLLER - SHOW - _watchNextEpisode');			
	}

	vm.types = [
	    {value:"ANIMATED",i18n:'show.types.ANIMATED'},
	    {value:"DAILY",i18n:'show.types.DAILY'},
	    {value:"MINI_SERIES",i18n:'show.types.MINI_SERIES'},
	    {value:"SEASON",i18n:'show.types.SEASON'}
	    ];

	
	vm.timezones = [
	    {value:'EST', i18n:'show.timezones.EST'},
	    {value:'CST', i18n:'show.timezones.CST'},
	    {value:'PST', i18n:'show.timezones.PST'},
	    {value:'JST', i18n:'show.timezones.JST'},
	    {value:'AET', i18n:'show.timezones.AET'},    
	    {value:'Etc/GMT+12', i18n:'show.timezones.GMT+12'},
	    {value:'Etc/GMT+11', i18n:'show.timezones.GMT+11'},
	    {value:'Etc/GMT+10', i18n:'show.timezones.GMT+10'},
	    {value:'Etc/GMT+9', i18n:'show.timezones.GMT+9'},
	    {value:'Etc/GMT+8', i18n:'show.timezones.GMT+8'},
	    {value:'Etc/GMT+7', i18n:'show.timezones.GMT+7'},
	    {value:'Etc/GMT+6', i18n:'show.timezones.GMT+6'},
	    {value:'Etc/GMT+5', i18n:'show.timezones.GMT+5'},
	    {value:'Etc/GMT+4', i18n:'show.timezones.GMT+4'},
	    {value:'Etc/GMT+3', i18n:'show.timezones.GMT+3'},
	    {value:'Etc/GMT+2', i18n:'show.timezones.GMT+2'},
	    {value:'Etc/GMT+1', i18n:'show.timezones.GMT+1'},
	    {value:'Etc/GMT', i18n:'show.timezones.GMT'},
	    {value:'Etc/GMT-1', i18n:'show.timezones.GMT-1'},
	    {value:'Etc/GMT-2', i18n:'show.timezones.GMT-2'},
	    {value:'Etc/GMT-3', i18n:'show.timezones.GMT-3'},
	    {value:'Etc/GMT-4', i18n:'show.timezones.GMT-4'},
	    {value:'Etc/GMT-5', i18n:'show.timezones.GMT-5'},
	    {value:'Etc/GMT-6', i18n:'show.timezones.GMT-6'},
	    {value:'Etc/GMT-7', i18n:'show.timezones.GMT-7'},
	    {value:'Etc/GMT-8', i18n:'show.timezones.GMT-8'},
	    {value:'Etc/GMT-9', i18n:'show.timezones.GMT-9'},
	    {value:'Etc/GMT-10', i18n:'show.timezones.GMT-10'},
	    {value:'Etc/GMT-11', i18n:'show.timezones.GMT-11'},
	    {value:'Etc/GMT-12', i18n:'show.timezones.GMT-12'},
	    {value:'Etc/GMT-13', i18n:'show.timezones.GMT-13'},
	    {value:'Etc/GMT-14', i18n:'show.timezones.GMT-14'}
        ];
}