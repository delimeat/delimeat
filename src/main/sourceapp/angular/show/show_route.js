angular.module('delimeat.show')
	.config(['$routeProvider',
            function($routeProvider) {
              $routeProvider
              	.when('/show/:id', {
                  templateUrl: 'app/show/show.tmpl.html',
                  controller: 'ShowController',
                  controllerAs: 'vm'
                });
            }]);