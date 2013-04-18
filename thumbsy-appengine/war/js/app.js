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

var DEBUG = true;
var DEBUG_WEB = true;

var MAX_CHAR_PER_TEXT = 500;

var myApp = angular.module('myApp', []);
var postMessageUrl = "http://thumbsy-demo.appspot.com/sendAll";
var fetchConversationUrl = "http://thumbsy-demo.appspot.com/rest/messages/conversation/";

if (DEBUG) {
    postMessageUrl = "http://localhost:8888/sendAll";
    fetchConversationUrl = "http://localhost:8888/api/messages/conversation/";
}