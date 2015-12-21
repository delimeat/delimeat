angular
	.module('delimeat.show')
	.factory('ShowModel', ShowModel);

ShowModel.$inject = ['$log','NotificationsService','ShowService','AlertService'];

function ShowModel($log,NotificationsService,ShowService,AlertService) {
    
	var vm = this;
	
	vm._log = $log;
	
	vm._notificationsService = NotificationsService;
	vm._showService = ShowService;
	vm._alertService = AlertService;
	
	vm._show = null;
    
    return {
        read: read,
        update:update,
        remove:remove,
        get:get
    };
    
    function get(){
    	return vm._show;
    }
    
	function read(id){
		vm._log.debug('START - MODEL - SHOW - read');
		vm._notificationsService.notify("show.read.start");
		vm._show = null;
		vm._showService.read(id)
			.then(function(response){
				vm._log.debug('SUCCESS - MODEL - SHOW - read');
				vm._show = response;
				vm._notificationsService.notify("show.read.success");								
			},
			function(response){
				vm._log.debug('ERROR - MODEL - SHOW - read');
				vm._alertService.addError('Opps Something went wrong',5000);
				vm._notificationsService.notify("show.read.error");								
			})
			.finally(function(){
				vm._log.debug('FINALLY - MODEL - SHOW - read');
				vm._notificationsService.notify("show.read.end");								
			});
		vm._log.debug('END - MODEL - SHOW - read');

	}
	
	function update(show){
		vm._log.debug('START - MODEL - SHOW - update');
		vm._notificationsService.notify("show.update.start");
		vm._showService.update(show)
			.then(function(response){
				vm._log.debug('SUCCESS - MODEL - SHOW - update');
				vm._alertService.addSuccess('Show Updated',5000);
				vm._show = show;
				vm._notificationsService.notify("show.update.success");								
			},
			function(response){
				vm._log.debug('ERROR - MODEL - SHOW - update');
				vm._alertService.addError('Opps Something went wrong',5000);
				vm._notificationsService.notify("show.update.error");								
			})
		    .finally(function(){
				vm._log.debug('FINALLY - MODEL - SHOW - update');
				vm._notificationsService.notify("show.update.end");								
		    });
		vm._log.debug('END - MODEL - SHOW - update');

	}
	
	function remove(){
		vm._log.debug('START - MODEL - SHOW - remove');
		vm._notificationsService.notify("show.remove.start");	
		vm._log.debug(vm._show.showId);
		vm._showService.remove(vm._show.showId)
		.then(function(response){
			vm._log.debug('SUCCESS - MODEL - SHOW - remove');
			vm._notificationsService.notify("show.remove.success");
		},
		function(response){
			vm._log.debug('ERROR - MODEL - SHOW - remove');
			vm._alertService.addError('Opps Something went wrong',5000);
			vm._notificationsService.notify("show.remove.error");
		})
	    .finally(function(){
			vm._log.debug('FINALLY - MODEL - SHOW - remove');
			vm._notificationsService.notify("show.remove.end");								
	    });
		vm._log.debug('END - MODEL - SHOW - remove');
	}
	
}