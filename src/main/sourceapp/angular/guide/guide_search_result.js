angular
	.module('delimeat.guide')
	.controller('GuideSearchResultController',GuideSearchResultController);

GuideSearchResultController.$inject = ['$scope','$log','$location','GuideShowModel','NotificationsService'];

function GuideSearchResultController($scope, $log,$location,GuideShowModel,NotificationsService) {
	
	var vm = this;

	vm._scope = $scope;
	vm._log = $log;
	vm._location = $location;
	
	vm._guideShowModel = GuideShowModel;
	vm._notificationsService = NotificationsService;

	vm.add = add;
	
	_onLoad();	
	function _onLoad(){
		vm._log.info("START - DIRECTIVE - GUIDE SEARCH RESULT - _onLoad");
        vm._scope.$on('$destroy', _destroy);
		vm._notificationsService.add("search.create.success",_showCreated);
		vm._log.info("START - DIRECTIVE - GUIDE SEARCH RESULT - _onLoad");
	}
	
	function _destroy(){
		vm._log.info('START - DIRECTIVE - GUIDE SEARCH RESULT - _destroy');
		vm._notificationsService.remove("search.create.success",_showCreated);
		vm._log.info('END - DIRECTIVE - GUIDE SEARCH RESULT - _destroy');
	}
	
	function _showCreated(){
		vm._log.info('START - DIRECTIVE - GUIDE SEARCH RESULT - _created');
		var show = vm._guideShowModel.get();
		vm._location.path( "/show/"+show.showId );
		vm._log.info('START - DIRECTIVE - GUIDE SEARCH RESULT - _created');
	}
	
	function add(){
		vm._log.info('START - DIRECTIVE - GUIDE SEARCH RESULT - add');
		var show = {
					airTime: 1,
					timezone: "timezone",
					title: vm.result.title,
					airing: true,
					showType: "SEASON",
					lastGuideUpdate: null,
					lastFeedUpdate: null,
					enabled: true,
					nextEpisode: null,
					previousEpisode: null,
					includeSpecials: true,
					version: 0,
					guideSources: [] 
				    };
		
		if(vm.result.guideIds!=null && vm.result.guideIds.length>0){
			for(var i=0; i< vm.result.guideIds.length; i++){
				var guideId = vm.result.guideIds[i];
				var guideSource = {
									guideSource: guideId.source,
									guideId: guideId.value
									}
				show.guideSources.push(guideSource);
			}
		}
		//TODO add functionality to set next/previous episode (maybe on server side)
		
		vm._log.debug(show);
		vm._guideShowModel.create(show);
		vm._log.info('END - DIRECTIVE - GUIDE SEARCH RESULT - add');
	}
}

angular
.module('delimeat.guide')
.directive('deliGuideSearchResult',GuideSearchResultDirective);

function GuideSearchResultDirective() {
    var directive = {
            restrict: 'EA',
            templateUrl: 'app/guide/guide_search_result.tmpl.html',
            scope: {
                result: '='
            },
            controller: GuideSearchResultController,
            controllerAs: 'vm',
            bindToController: true,
            transclude:true
        };
	
	return directive;
}

