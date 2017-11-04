angular
	.module('delimeat.shared')
	.factory('AlertService', AlertService);
	
AlertService.$inject = ['$rootScope','$timeout'];

function AlertService($rootScope, $timeout) {

	$rootScope.alerts = [];
	
	var addAlert =  function addAlert(type, msg, timeout) {
    	$rootScope.alerts.push({
            type: type,
            msg: msg,
            close: function() {
            	var index = $rootScope.alerts.size-1;
                return closeAlert(index);
            }
        });
    	if(timeout){
            $timeout(function(){ 
            	var index = $rootScope.alerts.size-1;
                return closeAlert(index);
            }, timeout);
    	}
    };
	
    return {
        addSuccess: addSuccess,
        addError: addError,
        addInfo: addInfo,
        addWarning: addWarning,
        closeAlert: closeAlert,
        closeAll: closeAll
    };

    function addSuccess(msg,timeout){
    	return addAlert("success", msg, timeout);
    }
    function addError(msg,timeout){
    	return addAlert("danger", msg, timeout);
    }
    function addInfo(msg,timeout){
    	return addAlert("info", msg, timeout);
    }
    function addWarning(msg,timeout){
    	return addAlert("warning", msg, timeout);
    }
    function closeAlert(index) {
        return $rootScope.alerts.splice(index, 1);
    }
    function closeAll(){
    	$rootScope.alerts = [];
    }

}