/*
 javascript file for thumbsy app

 */

$(document).ready(function () {
//    $("p").click(function() {
//        $(this).hide();
//    });

});

var myApp = angular.module('myApp', []);

myApp.factory('Data', function () {
    return {message: "I'm data from a service"};
});


function MessageCtrl($scope, $http, Data) {

    $scope.data = Data;


    $scope.messages = [
        {text: 'blah blah', incoming: false},
        {text: 'umbala', incoming: true}
    ];

    $scope.addMessage = function (messageContent) {
        $scope.messages.push({content: messageContent, incoming: false});
        messageContent = '';

        $http.post("http://example.appspot.com/rest/app", {"foo": "bar"})
            .success(function (data, status, headers, config) {
                $scope.data = data;
            }).error(function (data, status, headers, config) {
                $scope.status = status;
            });

    };

    $scope.getTotalMessages = function () {
        return $scope.messages.length;
    };

    $scope.deleteMessage = function (index) {
        $scope.messages.splice(index, 1);
    };

    $scope.fetchMessages = function (conversationId) {

        $scope.code = null;
        $scope.response = null;

        conversationId = '';
        $http.get('conversations/' + conversationId)
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
}

function AnotherCtrl($scope, Data) {
    $scope.data = Data;
}