/*
 javascript file for thumbsy app

 */

var DEBUG = false;
var DEBUG_WEB = false;

var MAX_CHAR_PER_TEXT = 500;

var myApp = angular.module('myApp', []);
var postMessageUrl = "http://thumbsy-demo.appspot.com/sendAll";
var fetchConversationUrl = "http://thumbsy-demo.appspot.com/rest/messages/conversation/";

if (DEBUG) {
    postMessageUrl = "http://localhost:8888/sendAll";
    fetchConversationUrl = "http://localhost:8888/rest/messages/conversation/";
}

var currentConvId = 1;

//var scopeConvCtrl = angular.element("#ConversationCtrl").scope;


myApp.factory('Data', function () {
    return {message: "I'm data from a service"};
});

// global scope of a ctrl


function ConversationCtrl($scope, $http, Data) {

    // retrieve global data from different controllers
    $scope.data = Data;

    // array of messages for this conversation
    $scope.messages = [
//        {content: 'blah blah', incoming: false},
//        {content: 'umbala', incoming: true}
    ];


    $scope.addMessage = function (messageContent) {

        convId = 1;
        msgId = $scope.messages.length + 1;


        var jsonObj = {id: msgId, conversationId: convId, content: messageContent, incoming: false};

        $http.post(postMessageUrl, jsonObj)
            .success(function (respData, respStatus, headers, config) {
                $scope.respData = respData;
                $scope.respStatus = respStatus;

                // NOTE: push temporarily
                // confirm when received notice from android client
                // if didn't send, should delete
                // and then set input textbox to the message
                $scope.messages.push(jsonObj);
                $("message-box").prop("scrollHeight");
                $scope.messageContent = '';

            }).error(function (respData, respStatus, headers, config) {
                $scope.respData = respData;
                $scope.respStatus = respStatus;
                alert("Error sending message!!!\n" + respStatus);

            });
    };

    $scope.fetchConversation = function (conversationId) {

        $scope.code = null;
        $scope.response = null;
        conversationId = currentConvId;

        $http.get(fetchConversationUrl + conversationId)
            .success(function (respData, respStatus) {

                $scope.respStatus = respStatus;
                $scope.respData = respData;

                $scope.conversation = respData.message;

                for (var i = 0; i < respData.message.length; i++) {
                    var message = respData.message[i];
                    $scope.messages.push({content: message.content, incoming: message.incoming});
                }
                $("message-box").prop("scrollHeight");
            }).error(function (data, status) {

                $scope.status = status;
                $scope.data = data;

                alert("error fetching message");
            });


    };

    $scope.getTotalMessages = function () {
        return $scope.messages.length;
    };

    $scope.getCharCount = function (messageContent) {
        return MAX_CHAR_PER_TEXT - ("" + messageContent).trim().length;
    };

    $scope.deleteMessage = function (index) {
        $scope.messages.splice(index, 1);
    };

    if (!DEBUG_WEB) {
        // fetch conversation on page load
        $scope.$on('$viewContentLoaded', $scope.fetchConversation(1));
    }

    function AnotherCtrl($scope, Data) {
        $scope.data = Data;
    }
}

$(document).ready(function () {
//    $("p").click(function() {
//        $(this).hide();
//    });
});


