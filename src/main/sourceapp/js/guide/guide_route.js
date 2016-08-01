var GuideModule = angular.module('delimeat.guide');

GuideModule.config(GuideRouteConfig);

GuideRouteConfig.$inject = ['$routeProvider'];

function GuideRouteConfig($routeProvider) {
    $routeProvider.
    	when('/guide/search', {
    		templateUrl: 'js/guide/guide_search.tmpl.html',
    		controller: 'GuideSearchController',
    		controllerAs: 'vm',
    		resolve: {
    			results: ['$route','GuideService', function($route, GuideService){
    				if(angular.isString($route.current.params.title)){
    					return GuideService.search({title:$route.current.params.title}).$promise;
    				}else{
    					return [];
    				}
    			}],
    			title: ['$route', function($route){
    				return $route.current.params.title;
    			}]
    		}
    	}).
    	when('/guide/info/:id', {
    		templateUrl: 'js/guide/guide_info.tmpl.html',
    		controller: 'GuideInfoController',
    		controllerAs: 'vm',
    		resolve: {
    			data: ['$route','$filter','$q','GuideService', function($route,$filter,$q, GuideService){
    		        var infoPromise = GuideService.info({id:$route.current.params.id}).$promise;
    		        var epPromise = GuideService.episodes({id:$route.current.params.id}).$promise;		 
    		        
    		        return $q.all([infoPromise, epPromise]).then(function(results){	
    		        	var info = results[0];
    		        	var episodes = results[1];
    		    		var nextEpisode = null;
    		    		var prevEpisode = null;
    		    		if(angular.isArray(episodes) && episodes.length > 0){
    		    			
    		    			episodes = $filter('orderBy')(episodes, ['seasonNum', 'episodeNum','airDate'], true);
            		        
    		    			var curDate = new Date();
    		    			for(var i = 0; i< episodes.length; i++){
    		    				var episode = episodes[i];
    		    				var tzDate = $filter('applytz')(episode.airDate + ' ' + info.airTime, info.timezone);
    		    				var airDateTime = new Date(tzDate);
    		    				  if(curDate >= airDateTime){
    		    					  break;
    		    				  }
    		    				nextEpisode = episode;
    		    				console.log(nextEpisode);
    		    			}
    		    			
    		    			var nextIdx = episodes.indexOf(nextEpisode);			
    		    			prevEpisode = angular.isObject(episodes[nextIdx + 1]) ? episodes[nextIdx + 1] : null;

    		    		}
    		    		
    		    		var show = {
    							guideId: info.guideId,
    							airTime: info.airTime,
    							timezone: info.timezone,
    							title: info.title,
    							airing: info.airing,
    							showType: "SEASON",
    							lastGuideUpdate: null,
    							lastFeedUpdate: null,
    							enabled: true,
    							nextEpisode: nextEpisode,
    							previousEpisode: prevEpisode,
    							includeSpecials: false,
    							minSize: (angular.isNumber(info.runningTime) ? info.runningTime * 3.2 : 80),
    							maxSize: (angular.isNumber(info.runningTime) ? info.runningTime * 14 : 700)
    							};
	    				
	    				if(angular.isArray(info.airDays) && info.airDays.length > 1){
	    					show.showType="DAILY";
	    				}	
    		            return {
    		                info: info,
    		                episodes: episodes,
    		                show: show
    		            };
    		        });
    			}]
    		}
    	});
 }
