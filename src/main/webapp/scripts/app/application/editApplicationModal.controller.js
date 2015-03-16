'use strict';

angular.module('imeApp').controller('EditApplicationModalController', 
		function($timeout, $scope, $modal, $modalInstance, application, field) {		
	
	$scope.application = {};
	angular.extend($scope.application, application);
	
	$scope.field = field;	    	                 
	
	$scope.save = function () {
		
		$modalInstance.close($scope.application);
	};

	$scope.delete = function () {		
		
		var confirmModalInstance = $modal.open({			
			templateUrl: 'scripts/app/application/deleteApplicationModal.html',
			controller: 'DeleteApplicationModalController',
			size: 'sm',
			resolve: {
				application: function() {
					return $scope.application;
				}
			}
		});
		
		confirmModalInstance.result.then(function() {
			
			// close modal and pass back application okay'ed for deletion
			$modalInstance.close(
					{
						application: $scope.application, 
						remove: true
					}
			);
		});
				
	};	
	
	$scope.cancel = function () {
		
		$modalInstance.dismiss('cancel');
	};		
	
});