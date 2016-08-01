angular
.module('delimeat.shared')
.filter('applytz', ApplyTzFilter);

ApplyTzFilter.$inject = ['$log'];

function ApplyTzFilter($log) {
	
	vm = this;
  
	vm.timezones = [
	        	    {value:'EST', offset:'-0500'},
	        	    {value:'CST', offset:'-0600'},
	        	    {value:'PST', offset:'-0700'},
	        	    {value:'JST', offset:'+0900'},
	        	    {value:'AET', offset:'+1000'},    
	        	    {value:'Etc/GMT+12', offset:'+1200'},
	        	    {value:'Etc/GMT+11', offset:'+1100'},
	        	    {value:'Etc/GMT+10', offset:'+1000'},
	        	    {value:'Etc/GMT+9', offset:'+0900'},
	        	    {value:'Etc/GMT+8', offset:'+0800'},
	        	    {value:'Etc/GMT+7', offset:'+0700'},
	        	    {value:'Etc/GMT+6', offset:'+0600'},
	        	    {value:'Etc/GMT+5', offset:'+0500'},
	        	    {value:'Etc/GMT+4', offset:'+0400'},
	        	    {value:'Etc/GMT+3', offset:'+0300'},
	        	    {value:'Etc/GMT+2', offset:'+0200'},
	        	    {value:'Etc/GMT+1', offset:'+0100'},
	        	    {value:'Etc/GMT', offset:'+0000'},
	        	    {value:'Etc/GMT-1', offset:'-0100'},
	        	    {value:'Etc/GMT-2', offset:'-0200'},
	        	    {value:'Etc/GMT-3', offset:'-0300'},
	        	    {value:'Etc/GMT-4', offset:'-0400'},
	        	    {value:'Etc/GMT-5', text:'-0500'},
	        	    {value:'Etc/GMT-6', offset:'-0600'},
	        	    {value:'Etc/GMT-7', offset:'-0700'},
	        	    {value:'Etc/GMT-8', offset:'-0800'},
	        	    {value:'Etc/GMT-9', offset:'-0900'},
	        	    {value:'Etc/GMT-10', offset:'-1000'},
	        	    {value:'Etc/GMT-11', offset:'-1100'},
	        	    {value:'Etc/GMT-12', offset:'-1200'},
	        	    {value:'Etc/GMT-13', offset:'-1300'},
	        	    {value:'Etc/GMT-14', offset:'-1400'}
	                ];
	
	return function(input, tz) {
		var offset = "+0000";
		for(var i = 0; i< vm.timezones.length; i++){
			if (vm.timezones[i].value === tz) {
				offset = vm.timezones[i].offset;
				break;
			}
		}
		return moment(input + ' ' + offset, "YYYY-MM-DD hh:mm a Z").format();
	};
}