angular
	.module('delimeat.guide')
	.factory('GuideSearchModel', GuideSearchModel);

GuideSearchModel.$inject = ['$log','NotificationsService','GuideService','AlertService'];

function GuideSearchModel($log,NotificationsService,GuideService,AlertService) {
    
	var vm = this;
	
	vm._log = $log;

	vm._notificationsService = NotificationsService;
	vm._guideService = GuideService;
	vm._alertService = AlertService;
	
	vm._results = {};
    
    return {
        read: read,
        get:get
    };
    
    function get(){
    	return vm._results;
    }
    
	function read(title){
		vm._log.debug('START - MODEL - GUIDE SEARCH - read');
		vm._notificationsService.notify("search.read.start");
		vm._results = {};
		vm._guideService.read(title)
			.then(function(response){
				vm._log.debug('SUCCESS - MODEL - GUIDE SEARCH - read');
				vm._results = response;	
		        if(!vm._results||vm._results.length===0){
		          	 AlertService.addWarning('No Results Found',5000);
		           }
				vm._notificationsService.notify("search.read.success");								
			},
			function(response){
				vm._log.debug('ERROR - MODEL - GUIDE SEARCH - read');
				vm._alertService.addError('Opps Something went wrong',5000);
				vm._notificationsService.notify("search.read.error");								
			})
			.finally(function(){
				vm._log.debug('FINALLY - MODEL - GUIDE SEARCH - read');
				vm._notificationsService.notify("search.read.end");								
			});
		vm._log.debug('END - MODEL - GUIDE SEARCH - read');
	}
	
}