angular.module('delimeat.shared')
	.directive('deliWorking',WorkingDirective);

function WorkingDirective() {
    var directive = {
        restrict: 'EA',
        template: '<div ng-show="vm.working" layout="row" layout-sm="column" layout-align="center center"  style="position:absolute;height: 100%;width: 100%;z-index: 1;top: -9999999px;left: -9999999px;width: 100%;height: 100%;background-color: rgba(255,255,255,.85);opacity: 0;transition: opacity 500ms ease-in;"><md-progress-circular md-mode="indeterminate"></md-progress-circular></div><ng-transclude></ng-transclude>',
        scope: {
            start: '@',
            end: '@'
        },
        controller: WorkingController,
        controllerAs: 'vm',
        bindToController: true,
        transclude:true
    };

    return directive;
}

angular
	.module('delimeat.shared')
	.controller('WorkingController', WorkingController);

WorkingController.$inject = ['$log','$scope','NotificationsService' ];

function WorkingController($log, $scope, NotificationsService ) {
	
  	var vm = this;

	vm._log = $log;
	vm._scope = $scope;
	vm._notifications = NotificationsService;

	vm.working = false;

	vm._start = _start;
	vm._end = _end;
	vm._destroy = _destroy;

	_onLoad();
	function _onLoad() {
		vm._log.debug('START - DIRECTIVE - WORKING - _onLoad');
		vm._scope.$on('$destroy', vm._destroy.bind(vm));
		vm._notifications.add(vm.start, vm._start.bind(vm));
		vm._notifications.add(vm.end, vm._end.bind(vm));
		vm._log.debug('END - DIRECTIVE - WORKING - _onLoad');
	}

	function _destroy() {
		vm._log.debug('START - DIRECTIVE - WORKING - _destroy');
		vm._notifications.remove(vm.start, vm._start.bind(vm));
		vm._log.debug('END - DIRECTIVE - WORKING - _destroy');

	}

	function _start() {
		vm._log.debug('START - DIRECTIVE - WORKING - _start');
		vm.working = true;
		vm._log.debug('END - DIRECTIVE - WORKING - _start');
	}

	function _end() {
		vm._log.debug('START - DIRECTIVE - WORKING - _end');
		vm.working = false;
		vm._log.debug('END - DIRECTIVE - WORKING - _end');
	}

}
