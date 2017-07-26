angular
	.module('delimeat.shows')
	.controller('ShowController', ShowController);

ShowController.$inject = ['$log', '$location', '$window','$translate', 'show'];

function ShowController($log, $location, $window, $translate, show) {

	var vm = this;	

	vm._log = $log;
	vm._location = $location;
	vm._window = $window;
	vm._translate = $translate;

	vm.remove = remove;
	vm.update = update;

	vm.show = show;
	
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

	vm.types = [
	    {value:"ANIMATED",i18n:'show.types.ANIMATED'},
	    {value:"DAILY",i18n:'show.types.DAILY'},
	    {value:"MINI_SERIES",i18n:'show.types.MINI_SERIES'},
	    {value:"SEASON",i18n:'show.types.SEASON'}
	    ];

	
	vm.timezones = [
	    {value:'Etc/GMT-5', offset:'-0500'},
	    {value:'America/Chicago', offset:'-0600'},
	    {value:'America/Los_Angeles', offset:'-0700'},
	    {value:'Asia/Tokyo', offset:'+0900'},
	    {value:'Australia/Sydney', offset:'+1000'},     
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