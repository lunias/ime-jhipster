angular.module('imeApp').factory('Application', function($resource) {
	
    return $resource('api/applications/:id', {}, {
    	
        'findAll': { method: 'GET', isArray: false,
        	interceptor: {
                response: function(response) {
                    return {                    	
                    	body: response.data._embedded['ime:applicationResourceList'],
                    	links: response.data._links,
                    	page: response.data.page
                    };
                }        		
        	}
        },
        
        'findAllMedia' : { method: 'GET', isArray: false,
        	url: 'api/applications/:id/media',
        	interceptor: {
                response: function(response) {
                    return {                    	
                    	body: response.data._embedded['ime:mediaResourceList'],
                    	links: response.data._links,
                    	page: response.data.page
                    };
                }        		
        	}        	
        },
        
        'update': { method: 'PUT' },
        'remove': { method: 'DELETE' }
    });	
});