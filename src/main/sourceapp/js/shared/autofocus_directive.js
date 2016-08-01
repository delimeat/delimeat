/* global angular */
/*
(function () {
  'use strict';

  angular.module('delimeat.shared')

  .directive('autofocus', ['$timeout',
    function ($timeout) {
      return {
        restrict: 'A',
        link: function ($scope, $element) {
          $timeout(function () {
            $element[0].focus();
          });
        }
      };
    }
  ]);
})();
*/


angular
.module('delimeat.shared')
.directive('autofocus', AutofocusDirective);

AutofocusDirective.$inject = ['$timeout'];


function AutofocusDirective($timeout) {
    return {
      restrict: 'A',
      link: function ($scope, $element) {
        $timeout(function () {
          $element[0].focus();
          $element[0].select();
        });
      }
    };
}

