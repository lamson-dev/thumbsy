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

var appFilters = angular.module('myApp.filters', []);

appFilters.filter('profilePicture', function () {
    return function (profilePicUrl, size) {
        if (profilePicUrl) {
            var clean = profilePicUrl.replace(/\?sz=(\d)*$/, '');
            return clean + '?sz=' + size;
        } else {
            return '';
        }
    };
});

appFilters.filter('threadDate', function () {
    return function (dateInMilliSec) {
        var date = moment(parseInt(dateInMilliSec));
        if (date.diff(moment(), 'days') > 0)
            return date.format('MMM Do, h:mm A');
        else
            return date.format('h:mm A');
    };
});