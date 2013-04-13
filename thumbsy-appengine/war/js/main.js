/*
 javascript file for thumbsy app

 */

$(document).ready(function () {
//    $("p").click(function() {
//        $(this).hide();
//    });
});

var myApp = angular.module('myApp', []);
var postMessageUrl = "http://thumbsy-demo.appspot.com/sendAll";
var getConversationUrl = "http://thumbsy-demo.appspot.com/conversation";

myApp.factory('Data', function () {
    return {message: "I'm data from a service"};
});

function ConversationCtrl($scope, $http, Data) {

    $scope.data = Data;

    $scope.messages = [
        {content: 'blah blah', incoming: false},
        {content: 'umbala', incoming: true}
    ];

    $scope.addMessage = function (messageContent) {

        var jsonObj = {content: messageContent, incoming: false};
        $("message-box").prop("scrollHeight");

        $http.post(postMessageUrl, jsonObj)
            .success(function (data, status, headers, config) {

                $scope.data = data;
                $scope.messages.push(jsonObj);
                $scope.messageContent = '';

            }).error(function (data, status, headers, config) {

                $scope.status = status;
                alert("failed sending message");

            });
    };

    $scope.fetchConversation = function (conversationId) {

        $scope.code = null;
        $scope.response = null;

        conversationId = '';
        $http.get(getConversationUrl + conversationId)
            .success(function (data, status) {

                $scope.status = status;

                $scope.conversation = data;
                $scope.messages = data.messages;

                for (var i = 0; i < $scope.messages.length; i++) {
                    var message = $scope.messages[i];
                    $scope.messages.push({content: message.text, incoming: message.incoming});
                }
            }).error(function (data, status) {

                $scope.status = status;
                $scope.data = data;

                alert("error fetching message");
            });


    };

    $scope.getTotalMessages = function () {
        return $scope.messages.length;
    };

    $scope.deleteMessage = function (index) {
        $scope.messages.splice(index, 1);
    };
}

function AnotherCtrl($scope, Data) {
    $scope.data = Data;
}