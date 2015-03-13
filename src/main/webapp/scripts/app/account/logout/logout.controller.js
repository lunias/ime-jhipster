'use strict';

angular.module('imeApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
