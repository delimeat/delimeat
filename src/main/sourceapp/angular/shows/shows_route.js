var settingsModule = angular.module('delimeat.shows');

settingsModule.config(['$routeProvider',
            function($routeProvider) {
              $routeProvider
                .when('/shows', {
                	templateUrl: 'app/shows/shows.tmpl.html',
                	controller: 'ShowsController',
                	controllerAs: 'vm'
                })
                .otherwise({
                    redirectTo: '/shows'
                  });
            }]);