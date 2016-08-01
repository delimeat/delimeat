/* global angular */

angular
.module('delimeat.shared')
.directive('dmEpisode', EpisodeDirective);

EpisodeDirective.$inject = ['$translate','$filter','$log'];

function EpisodeDirective($translate,$filter,$log) {
	
    return {
      restrict: 'E',
      scope: {
    	  episode: '=',
          airtime: '=',
          timezone: '=',
          headingclass: '=',
          textclass: '=',
          type: '='
        },
      templateUrl: 'js/shared/episode.tmpl.html'
    };
}