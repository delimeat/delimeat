angular
	.module('delimeat.shows')
	.factory('ShowsModel', ShowsModel);

ShowsModel.$inject = ['NotificationsService','ShowsService','AlertService','$log'];

function ShowsModel(NotificationsService,ShowsService,AlertService,$log) {
    
	var vm = this;

	vm._log = $log;
	
	vm._notificationsService = NotificationsService;
	vm._showsService = ShowsService;
	vm._alertService = AlertService;
	
	vm._shows = {};
    
    return {
        read: read,
        get: get
    };
    
    function get(){
    	return vm._shows;
    }
    
	function read(){
		vm._log.debug('START - MODEL - SHOWS - load');
		vm._notificationsService.notify("shows.fetch.start");
		vm._showsService.get()
			.then(function(data,status){
				vm._log.debug('SUCCESS - MODEL - SHOWS - load');
				vm._shows = data;
			},
			function(data,status){
				vm._log.debug('ERROR - MODEL - SHOWS - load');
				vm._alertService.addError('Opps Something went wrong',5000);
			})
			.finally(function(){
				vm._log.debug('FINALLY - MODEL - SHOWS - load');
				vm._notificationsService.notify("shows.fetch.end");				
			});
		vm._log.debug('END - MODEL - SHOWS - load');

	}
}