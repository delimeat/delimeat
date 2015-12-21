angular
	.module('delimeat.shared')
	.factory('DialogService', DialogService);
	
DialogService.$inject = ['$mdDialog'];

function DialogService($mdDialog) {

	
    return {
        alert: alert,
        confirm: confirm
    };

    function alert(){
    	return $rootScope.working;
    }
    
    function confirm(title,content,ok, cancel){
		var dialog = $mdDialog.confirm({
	        title: title,
	        content: content,
	        ok: ok,
	        cancel: cancel
	      });
		return $mdDialog.show(dialog);
    }

}