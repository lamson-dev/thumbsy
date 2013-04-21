/*
 * Copyright (c) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

'use strict';

//myApp.directive('enter', function () {
//    return function (scope, element, attrs) {
//        element.bind('mouseenter', function () {
//            element.addClass(attrs.enter);
//        })
//    }
//});
//
//myApp.directive('leave', function () {
//
//    return function (scope, element, attrs) {
//        element.bind('mouseleave', function () {
//            element.removeClass(attrs.enter);
//        })
//    }
//});

var appDirectives = angular.module('myApp.directives', ['myApp.services']);


appDirectives.directive('enter', function () {
    return function (scope, element, attrs) {
        element.bind('mouseenter', function () {
            element.addClass(attrs.enter);
        })
    }
});

appDirectives.directive('leave', function () {

    return function (scope, element, attrs) {
        element.bind('mouseleave', function () {
            element.removeClass(attrs.enter);
        })
    }
});

