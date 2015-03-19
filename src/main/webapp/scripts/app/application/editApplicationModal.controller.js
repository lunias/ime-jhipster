'use strict';

angular.module('imeApp').controller('EditApplicationModalController', 
		function($timeout, $scope, $templateCache, $modal, $modalInstance, uiGridConstants, application, field, Application) {		
	
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
			size: 'md',
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
	
	/*
	 * media grid
	 */
	
	$templateCache.put('media-grid-row',
			"<div ng-click=\"$parent.getExternalScopes().rowClick(row.entity, col.field)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell hover-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	);	
	
	$scope.mediaGridOptions = {
		externalScope: $scope.gridScope,
		infiniteScrollPercentage : 15,
		showFooter: false,
		enableFiltering : true,
		useExternalSorting: true,
		enableRowSelection: true,
		enableSelectAll: false,
		columnDefs : [ {
			field : 'mediaValue',			
			allowCellFocus: false,
			filter: {
				condition: uiGridConstants.filter.CONTAINS,
				placeholder: 'media contains...'
			}
		} ],
		enableHorizontalScrollbar: false,
		onRegisterApi : function(gridApi) {					
			
			$scope.gridApi = gridApi;
			
			gridApi.infiniteScroll.on.needLoadMoreData($scope, function() {
				
				if ($scope.currentPage < $scope.totalPages - 1) {
					++$scope.currentPage;
				} else {
					return;
				}
				
				updateMediaGrid(false, function(data) {
					gridApi.infiniteScroll.dataLoaded();
					
				}, function(err) {
					gridApi.infiniteScroll.dataLoaded();
				});
			});
			
			gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {						
				
				$scope.sortField = sortColumns.length ? sortColumns[0].field : '';
				$scope.sortDir = sortColumns.length ? sortColumns[0].sort.direction : '';													
				
				scrollTopAndReloadGridData(gridApi);
			});
		} 
	};        
    
	function scrollTopAndReloadGridData(gridApi) {
		
		var grid = gridApi.grid;
		
		$scope.currentPage = 0;				
		gridApi.cellNav
		.scrollTo(
				grid,
				$scope,
				$scope.mediaGridOptions.data[0],
				$scope.mediaGridOptions.columnDefs[0]);
		
		updateMediaGrid(true);
		grid.options.loadTimout = false;
	}
	
	function updateMediaGrid(replaceData, success, err) {
		
		var replaceData = replaceData || false;
		var success = success || angular.noop;
		var error = err || angular.noop;
		
		var selfLink = application._links.self.href;	  						  					
		var id = selfLink.substr(selfLink.lastIndexOf('/') + 1);		
		
		var params = {
				id: id,
				page: $scope.currentPage
		};
		
		if ($scope.sortField) params.sort = $scope.sortField + ',' + $scope.sortDir;
		
		Application.findAllMedia(params, function(response) {
		
			$scope.currentPage = response.page.number;
			$scope.totalPages = response.page.totalPages;
			$scope.gridScope.totalElements = response.page.totalElements;
			
			if (replaceData) {
				$scope.mediaGridOptions.data = response.body;						
			} else {						
				$scope.mediaGridOptions.data = response.body.reduce(function(coll, item) {
					coll.push(item);
					return coll;
				}, $scope.mediaGridOptions.data);
			}
			
			success(response);
			
		}, function(err) {					
			error(err);					
		});								
	}
	
	$scope.currentPage = 0;
	$scope.totalPages = 0;
	$scope.sortField = '';
	$scope.sortDir = '';			
	updateMediaGrid();
	
	$scope.gridScope = {
			
			rowClick: function (media, field) {
				
				this.edit(media, field);
			},
	
	  		edit: function (media, field) {
	  			
	  			console.log('edit: ' + media.mediaValue);
	  		}			
	};	
	
});