angular.module('delimeat.guide')
	.config(['$routeProvider',
            function($routeProvider) {
              $routeProvider.
	              when('/search', {
	                  templateUrl: 'app/guide/guide_search.tmpl.html',
	                  controller: 'GuideSearchController',
	                  controllerAs: 'vm'
	                });
            }]);