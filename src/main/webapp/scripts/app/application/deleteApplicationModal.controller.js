'use strict';

angular.module('imeApp').controller('DeleteApplicationModalController', function($scope, $modalInstance, application) {

	$scope.application = application;
	
	$scope.confirmDelete = function () {
		
		$modalInstance.close();
	};
	
	$scope.cancelDelete = function() {
		
		$modalInstance.dismiss('cancel');
	};	
	
});