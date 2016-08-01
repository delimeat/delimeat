/* global angular */
angular
.module('delimeat.shared')
.directive('dmBackBtn', BackDirective);

BackDirective.$inject = ['$window'];


function BackDirective($window) {
    return {
        restrict: 'E',
        template: '<a type="button" class="btn btn-link" translate="common.back"></a>',
        link: function (scope, elem, attrs) {
            elem.bind('click', function () {
                $window.history.back();
            });
        }
    };
}
