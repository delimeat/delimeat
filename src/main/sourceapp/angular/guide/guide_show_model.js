angular
	.module('delimeat.guide')
	.factory('GuideShowModel', GuideShowModel);

GuideShowModel.$inject = ['NotificationsService','GuideShowService','AlertService','$log'];

function GuideShowModel(NotificationsService,GuideShowService,AlertService,$log) {
    
	var vm = this;
	
	vm._notificationsService = NotificationsService;
	vm._guideShowService = GuideShowService;
	vm._alertService = AlertService;
	vm._log = $log;
	
	vm._show = null;
    
    return {
        create: create,
        get: get
    };
    
    function get(){
    	return vm._show;
    }
    
	function create(show){
		vm._log.debug('START - search model read');
		vm._log.debug(show);
		vm._notificationsService.notify("search.create.start");
		vm._results = {};
		vm._guideShowService.create(show)
			.then(function(response){
				vm._log.debug('SUCCESS - search model create');
				vm._show = response;
				vm._notificationsService.notify("search.create.success");								
			},
			function(response){
				vm._log.debug('ERROR - search model create');
				vm._alertService.addError('Opps Something went wrong',5000);
				vm._notificationsService.notify("search.create.error");
				vm._log.debug(response);
			})
			.finally(function(){
				vm._notificationsService.notify("search.create.end");								
			});
	}
	
}