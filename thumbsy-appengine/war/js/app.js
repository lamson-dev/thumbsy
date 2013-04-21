'use strict';

var MAX_CHAR_PER_TEXT = 500;

var myApp = angular.module('myApp', ['myApp.services', 'myApp.directives', 'myApp.filters', 'ui'],
    function ($locationProvider) {
        $locationProvider.html5Mode(true);
    });

//angular.module('photoHunt',
//    ['photoHunt.services', 'photoHunt.directives', 'photoHunt.filters'],
//    function($locationProvider) {
//        $locationProvider.html5Mode(true);
//    }
//);