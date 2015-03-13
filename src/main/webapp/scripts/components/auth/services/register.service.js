'use strict';

angular.module('imeApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


