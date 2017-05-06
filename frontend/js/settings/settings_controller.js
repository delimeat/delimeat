angular
	.module('delimeat.settings')
	.controller('SettingsController' , SettingsController);

SettingsController.$inject = ['$scope','$log', '$location', 'settings'];

function SettingsController ($scope, $log, $location, settings) {
		
	var vm = this;
	
	// angular services
	vm._scope = $scope;
	vm._log = $log;
	vm._location = $location;
	
	// data
	vm.settings = settings;

	// functions
	vm.update = update;
	vm.removeFileType = removeFileType;
	vm.addFileType = addFileType;
	vm.addExcludedKeyword = addExcludedKeyword;
	vm.removeExcludedKeyword = removeExcludedKeyword;	

	function update(){
		vm._log.debug('START - CONTROLLER - SETTINGS - update');
		vm.settings.$update().then(function(){
			vm._location.path("/");
		});
		vm._log.debug('END - CONTROLLER - SETTINGS - update');
	}
	
	function removeFileType(index){
		vm._log.debug('START - CONTROLLER - SETTINGS - removeFileType');
		vm.settings.ignoredFileTypes.splice(index,1);
		vm.form.$setDirty();
		vm._log.debug('START - CONTROLLER - SETTINGS - removeFileType');		
	}
	
	function addFileType(){
		vm._log.debug('START - CONTROLLER - SETTINGS - addFileType');
		var extension = document.getElementById("extension").value;
		if(extension !== null && extension !== ""){
			var newFileType = extension.toUpperCase();
			if(vm.settings.ignoredFileTypes.indexOf(newFileType) === -1){
				vm.settings.ignoredFileTypes.push(newFileType);	
			}
			document.getElementById("extension").value = null;
			vm.form.$setDirty();
		}
		vm._log.debug('START - CONTROLLER - SETTINGS - addFileType');		
	}
	
	function removeExcludedKeyword(index){
		vm._log.debug('START - CONTROLLER - SETTINGS - removeExcludedKeyword');
		vm.settings.excludedKeywords.splice(index,1);
		vm.form.$setDirty();
		vm._log.debug('START - CONTROLLER - SETTINGS - removeExcludedKeyword');		
	}
	
	function addExcludedKeyword(){
		vm._log.debug('START - CONTROLLER - SETTINGS - addExcludedKeyword');
		var keyword = document.getElementById("excluded_keyword").value;
		if(keyword !== null && keyword !== ""){
			//var newFileType = keyword.toUpperCase();
			if(vm.settings.excludedKeywords.indexOf(keyword) === -1){
				vm.settings.excludedKeywords.push(keyword);	
			}
			document.getElementById("excluded_keyword").value = null;
			vm.form.$setDirty();
		}
		vm._log.debug('START - CONTROLLER - SETTINGS - addExcludedKeyword');		
	}

}
