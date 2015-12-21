var settingsModule = angular.module('delimeat.settings');

settingsModule.config(['$routeProvider',
            function($routeProvider) {
              $routeProvider.
                when('/settings', {
                  templateUrl: 'app/settings/settings.tmpl.html',
                  controller: 'SettingsController',
                  controllerAs: 'vm'
                });
            }]);