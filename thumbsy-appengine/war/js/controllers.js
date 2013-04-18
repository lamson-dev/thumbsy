function ConversationCtrl($scope, $http, Data) {

    // retrieve global data from different controllers
    $scope.data = Data;

    // array of messages for this conversation
    $scope.messages = [
    ];


    $scope.addMessage = function (messageContent) {

        var convId = 1;
        var address = "";

        var date = new Date().toLocaleString();

        var jsonObj = {
            userId: userId,
            threadId: convId,
            address: address,
            body: messageContent,
            incoming: false,
            date: date
        };

        $http.post(postMessageUrl, jsonObj)
            .success(function (respData, respStatus, headers, config) {
                $scope.respData = respData;
                $scope.respStatus = respStatus;

                // NOTE: push temporarily
                // confirm when received notice from android client
                // if didn't send, should delete
                // and then set input textbox to the message
                $scope.messages.push(jsonObj);
                $scope.messageContent = '';
//                $("#message-box").animate({ scrollTop: $('#message-box')[0].scrollHeight}, 1000);
                $("#message-box").animate({ scrollTop: $('#message-box').prop("scrollHeight")}, 1000);

            }).error(function (respData, respStatus, headers, config) {
                $scope.respData = respData;
                $scope.respStatus = respStatus;
                alert("Error sending message!!!\n" + respStatus);

            });
    };

    $scope.fetchConversation = function () {

        $('#spinner').show();

        $scope.code = null;
        $scope.response = null;

        var address = $scope.message.address;

        $http.get(fetchConversationUrl + userId + "/" + address)
            .success(function (respData, respStatus) {

                $scope.respStatus = respStatus;
                $scope.respData = respData;

                $scope.conversation = respData.sms;

                for (var i = 0; i < respData.sms.length; i++) {
                    var message = respData.sms[i];
                    $scope.messages.push(message);
                }

                $("#message-box").animate({ scrollTop: $('#message-box').prop("scrollHeight")}, 1000);

                $('#spinner').hide();

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

}


function ThumbsyCtrl($scope, Data) {
    $scope.data = Data;
}