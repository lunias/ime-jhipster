'use strict';

angular.module('imeApp')
    .controller('MainController', function ($scope, $q, $templateCache, $modal, Principal, Application) {
    	
		$scope.isAuthenticated = Principal.isAuthenticated;
		$scope.isInRole = Principal.isInRole;
    	
        Principal.identity().then(function(account) {
            $scope.account = account;
        });
        
		$templateCache.put('application-grid-row',
				"<div ng-click=\"$parent.getExternalScopes().rowClick(row.entity, col.field)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell hover-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
		);
		
		$templateCache.put('application-grid-footer',
				"<div class=\"ui-grid-bottom-panel\" style=\"text-align: right\; padding: 52px 5px 0px 0px;\">Total Applications: {{ getExternalScopes().totalElements }}</div>");			
		
		$scope.applicationGridOptions = {
			externalScope: $scope.gridScope,
			infiniteScrollPercentage : 15,
			showFooter : true,
			enableFiltering : true,
			useExternalSorting: true,
			enableRowSelection: false,
			columnDefs : [ {
				field : 'name',
				allowCellFocus: false
			}, {
				field : 'description',
				allowCellFocus: false					
			}, {
				field : 'status',
				allowCellFocus: false					
			} ],
			rowHeight: 50,
			rowTemplate: 'application-grid-row',
			footerTemplate: 'application-grid-footer',
			enableHorizontalScrollbar: false,
			onRegisterApi : function(gridApi) {					
				
				$scope.gridApi = gridApi;
				
				gridApi.infiniteScroll.on.needLoadMoreData($scope, function() {
					
					if ($scope.currentPage < $scope.totalPages - 1) {
						++$scope.currentPage;
					} else {
						return;
					}
					
					updateApplicationGrid(false, function(data) {
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
					$scope.applicationGridOptions.data[0],
					$scope.applicationGridOptions.columnDefs[0]);
			
			updateApplicationGrid(true);
			grid.options.loadTimout = false;
		}
		
		function updateApplicationGrid(replaceData, success, err) {
			
			var replaceData = replaceData || false;
			var success = success || angular.noop;
			var error = err || angular.noop;
			
			var params = {
					page: $scope.currentPage
			};
			
			if ($scope.sortField) params.sort = $scope.sortField + ',' + $scope.sortDir;
			
			Application.findAll(params, function(response) {
			
				$scope.currentPage = response.page.number;
				$scope.totalPages = response.page.totalPages;
				$scope.gridScope.totalElements = response.page.totalElements;
				
				if (replaceData) {
					$scope.applicationGridOptions.data = response.body;						
				} else {						
					$scope.applicationGridOptions.data = response.body.reduce(function(coll, item) {
						coll.push(item);
						return coll;
					}, $scope.applicationGridOptions.data);
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
		updateApplicationGrid();
		
		$scope.gridScope = {
				
				rowClick: function (application, field) {
					
					this.edit(application, field);
				},
		
		  		edit: function (application, field) {
		  			
		  			var modalInstance = $modal.open({
		  				templateUrl: 'scripts/app/application/editApplicationModal.html',
		  				controller: 'EditApplicationModalController',
		  				size: 'lg',
		  				resolve: {
		  					application: function () {
		  						return application;
		  					},
		  					field: function() {
		  						return field;
		  					}
		  				}
		  			});
		  			
		  			modalInstance.result.then(function (application) {			  							  							  						  			
		  				
		  				var remove;
		  				if (application.remove) {
		  					remove = application.remove;		  					
		  					application = application.application;
		  				}
		  				
	  					var selfLink = application._links.self.href;	  						  					
	  					var id = selfLink.substr(selfLink.lastIndexOf('/') + 1);	  							  				
	  					
		  				if (remove) {
		  					
		  					Application.remove({id: id}, function(deleteResponse) {
		  						
			  					scrollTopAndReloadGridData($scope.gridApi);
		  						
		  					}, function(err) {
		  						console.log(err);
		  					});
		  					
		  				} else {		  							  					
		  					
		  					delete application._links;
		  					
			  				Application.update({id: id}, application, function(updateResponse) {
			  					
			  					scrollTopAndReloadGridData($scope.gridApi);
			  					
			  				}, function(err) {
			  					console.log(err);
			  				});
		  				}			  				
		  				
		  			}, function () {
		  				console.log('Modal dismissed');
		  			});
		  		}			
		};
		
    });
