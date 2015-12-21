angular
	.module('delimeat.show')
	.factory('EpisodesModel', EpisodesModel);

EpisodesModel.$inject = ['$log','NotificationsService','EpisodesService','AlertService'];

function EpisodesModel($log,NotificationsService,EpisodesService,AlertService) {
    
	var vm = this;

	vm._log = $log;

	vm._notificationsService = NotificationsService;
	vm._episodesService = EpisodesService;
	vm._alertService = AlertService;
	
	vm._episodes = {};
    
    return {
        read: read,
        get:get
    };
    
    function get(){
    	return vm._episodes;
    }
    
	function read(id){
		vm._log.debug('START - MODEL - EPISODES - read');
		vm._notificationsService.notify("episodes.read.start");
		vm._show = null;
		vm._episodesService.read(id)
			.then(function(response){
				vm._log.debug('SUCCESS - MODEL - EPISODES - read');
				vm._episodes = response.episodes;			
				vm._notificationsService.notify("episodes.read.success");								
			},
			function(response){
				vm._log.debug('ERROR - MODEL - EPISODES - read');
				vm._alertService.addError('Opps Something went wrong',5000);
				vm._notificationsService.notify("episodes.read.error");								
			})
			.finally(function(){
				vm._log.debug('FINALLY - MODEL - EPISODES - read');
				vm._notificationsService.notify("episodes.read.end");								
			});
		vm._log.debug('END - MODEL - EPISODES - read');
	}
}