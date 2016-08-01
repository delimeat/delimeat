var app = angular.module('app', [
'ngRoute',
'ngResource',
'ngAnimate',
'ngCookies',
'ngSanitize',
'pascalprecht.translate',
'delimeat.config',
'delimeat.shared',
'delimeat.shows',
'delimeat.settings',
'delimeat.guide'
]);

app.config(TranslationConfig);

TranslationConfig.$inject = ['$translateProvider'];

function TranslationConfig($translateProvider) {

  $translateProvider.useStaticFilesLoader({
      prefix: 'i18n/locale-',
      suffix: '.json'
  });

  //$translateProvider.addInterpolation('$translateMessageFormatInterpolation');
  $translateProvider.preferredLanguage('en');
  $translateProvider.fallbackLanguage('en');
  //$translateProvider.useLocalStorage();
  $translateProvider.useLoaderCache(true);
  $translateProvider.useMissingTranslationHandlerLog();
  $translateProvider.useSanitizeValueStrategy('sanitize');
}

app.run(RouteScopeChange);

RouteScopeChange.$inject = ['$rootScope','$translate','$log','$window','$location'];

function RouteScopeChange($rootScope, $translate, $log, $window, $location){
	
	$rootScope.$on('$routeChangeStart', function(event, next, previous){
		$rootScope.working = true;
	});
	
    $rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
    	$rootScope.working = false;
    	if(angular.isString(current.$$route.title)){
	        $translate(current.$$route.title).then(function (translation) {
	        	$rootScope.title = translation + " - ";
	          });
        }else if(angular.isFunction(current.$$route.title)){
        	$rootScope.title = current.$$route.title(current.locals) + " - ";
        }else{
        	$rootScope.title = "";
        }
    });	
    
    $rootScope.$on('$routeChangeError', function (event, current, previous, rejection) {
    	$rootScope.working = false;
    	if (previous) {
    	    $window.history.back();
    	  } else {
    	    $location.path("/").replace();
    	  }
    });
}