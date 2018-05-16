function NotificationService() {
   
	var vm = this;

	vm._listeners = {};
  
    return {
        add: add,
        remove: remove,
        notify: notify
    };
    
    function add(type,listener){
      console.log("ADD - " + type);
        if(!vm._listeners[type]){
      		vm._listeners[type] = [];
        }
        vm._listeners[type].push(listener);
    }
   
    function remove(type,listener){
    	console.log("REMOVE - "+ type);
      if(vm._listeners[type]){
        var index = vm._listeners[type].indexOf(listener);
        if(index!==-1){
            vm._listeners[type].splice(index,1);
        }
      }
    }
  
    function notify(){
        var listeners;
      console.log("NOTIFY - " + arguments[0]);
        if(typeof arguments[0] !== 'string'){
            console.warn('EventDispatcher','First params must be an event type (String)');
        }else{
            listeners = vm._listeners[arguments[0]];

            for(var key in listeners){
                //This could use .apply(arguments) instead, but there is currently a bug with it.
                //listeners[key](arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5],arguments[6]);
                listeners[key](arguments[1],arguments[2],arguments[3],arguments[4],arguments[5],arguments[6]);
            }
        }
    }
}


angular
	.module('delimeat.shared')
   .provider('NotificationsService', NotificationsProvider);

function NotificationsProvider(){
	instance = null;
  
 this.$get = [function () {
   if(this.instance==null){ // jshint ignore:line
     this.instance = new NotificationService();
     this.instance._listeners = {};
   }
    return this.instance;
  }];
  
}
