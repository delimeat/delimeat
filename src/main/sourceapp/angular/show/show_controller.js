angular
	.module('delimeat.show')
	.controller('ShowController', ShowController);

ShowController.$inject = ['$scope','$routeParams','$log','$location','$mdDialog','ShowModel','NotificationsService','DialogService','EpisodesModel'];

function ShowController($scope,$routeParams,$log,$location, $mdDialog,ShowModel,NotificationsService,DialogService, EpisodesModel) {

	var vm = this;	

	vm._scope = $scope;
	vm._routeParams = $routeParams;
	vm._log = $log;
	vm._location = $location;
	vm._dialog = $mdDialog;

	vm.formScope = null;
	vm._showModel = ShowModel;
	vm._notificationsService = NotificationsService;
	vm._dialogService = DialogService;
	vm._episodesModel = EpisodesModel;

	vm.show = null;
	vm.origNextEp = null;
	vm.origPrevEp = null;
	
	vm.leave = leave;
	vm.remove = remove;
	vm.displayEps = displayEps;
	vm.setFormScope = setFormScope;
	
	_onLoad();
	function _onLoad(){
		vm._log.info('START - CONTROLLER - SHOW - _onLoad');
        vm._scope.$on('$destroy', _destroy);
		vm._notificationsService.add("show.read.success",_loadedShow);
		vm._notificationsService.add("show.remove.success",_returnHome);
		vm._notificationsService.add("show.update.success",_returnHome);
		vm._notificationsService.add("episodes.read.success",_loadedEps);
		vm._showModel.read(vm._routeParams.id);
		vm._log.info('END - CONTROLLER - SHOW - _onLoad');

	}
	
	function _destroy(){
		vm._log.info('START - CONTROLLER - SHOW - _destroy');
		vm._notificationsService.remove("show.read.success",_loadedShow);
		vm._notificationsService.remove("show.remove.success",_returnHome);
		vm._notificationsService.remove("show.update.success",_returnHome);
		vm._notificationsService.remove("episodes.read.success",_loadedEps);
		vm._log.info('END - CONTROLLER - SHOW - _destroy');
	}
	
	function _loadedShow(){
		vm._log.info('START - CONTROLLER - SHOW - _loadedShow');
		vm.show = vm._showModel.get();
		vm.title = vm.show.title;
		vm.origNextEp = vm.show.nextEpisode;
		vm.origPrevEp = vm.show.previousEpisode;
		vm._log.info('END - CONTROLLER - SHOW - _loadedShow');
	}
	
	function _returnHome(){
		vm._log.info('START - CONTROLLER - SHOW - _returnHome');
		vm._location.path("/");
		vm._log.info('END - CONTROLLER - SHOW - _returnHome');
	}
	
	function _loadedEps(){
		vm._log.info('START - CONTROLLER - SHOW - _loadedEps');
	    vm._dialog.show({
		      controller: EpisodesModalController,
		      controllerAs: vm,
		      clickOutsideToClose: true,
		      templateUrl: 'app/show/episodes_modal.tmpl.html',
		      parent: angular.element(document.body),
		      //targetEvent: event,
		      locals: {
		    	   episodes: vm._episodesModel.get(),
		           previousEpisode: vm.show.previousEpisode,
		           nextEpisode: vm.show.nextEpisode
		         }
		    })
		    .then(function(result) {
				vm._log.info('OK - CONTROLLER - SHOW - _loadedEps');
		    	vm.show.nextEpisode = result.nextEpisode;
		    	vm.show.previousEpisode = result.previousEpisode;
		    	vm.formScope.form.$dirty = true;
	      
		    }, function() {
				vm._log.info('CANCEL - CONTROLLER - SHOW - _loadedEps');

		    });
		vm._log.info('END - CONTROLLER - SHOW - _loadedEps');

	}
	
	function leave(){
		if(!vm.formScope.form.$valid){
			vm._dialogService.confirm(undefined,'Leave without making changes?','Yes','No')
			.then(function(){
					_returnHome();
				}
			);			
		}
		else if(vm.formScope.form.$dirty){
			vm._dialogService.confirm(undefined,'Do you want to save changes made?','Yes','No')
				.then(function(){
					if(vm.show.nextEpisode  && vm.origNextEp){
						vm.show.nextEpisode.version = vm.origNextEp.version;
					}
					if(vm.show.previousEpisode  && vm.origPrevEp){
						vm.show.previousEpisode.version = vm.origPrevEp.version;
					}
					vm._showModel.update(vm.show);
				},function(){
					_returnHome();
				}
			);
		}
		else {
			_returnHome();
		}
	}
	
	function remove(){
		vm._dialogService.confirm(undefined,'Do you want to remove '+ vm.title + '?','Yes','No')
		.then(function(){
			vm._showModel.remove();
		});
	}
	
	function displayEps() {
		vm._episodesModel.read(vm.show.guideId);
	}
	
	function setFormScope(scope){
		vm.formScope = scope;
	}
	
	vm.types = [
	    {value:'ANIMATED', text:'Animated'},
	    {value:'DAILY', text:'Daily'},
	    {value:'MINI_SERIES', text:'Mini Series'},
	    {value:'SEASON', text:'Season'}
	];
	    	
	vm.statuses = [
		{value:'AIRING', text:'Continuing'},
		{value:'ENDED', text:'Ended'}
	];
	
	vm.days = [
		{value:'DAILY', text:'Daily'},
		{value:'MONDAY', text:'Monday'},
		{value:'TUESDAY', text:'Tuesday'},
		{value:'WEDNESDAY', text:'Wednesday'},
		{value:'THURSDAY', text:'Thursday'},
		{value:'FIRDAY', text:'Friday'},
		{value:'SATURDAY', text:'Saturday'},
		{value:'SUNDAY', text:'Sunday'}
	];	
}