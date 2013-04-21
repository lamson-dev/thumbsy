"use strict";

function ThumbsyCtrl($scope, $location, Conf, ThumbsyApi) {

    // signIn
    $scope.userProfile = undefined;
    $scope.hasUserProfile = false;
    $scope.isSignedIn = false;
    $scope.immediateFailed = false;
    $scope.loading = false;

    $scope.currentConversationAddress = '';
    $scope.messages = [
//        {
//            "address": "+16823679168",
//            "body": "ok doi ti' xuong' lien`",
//            "date": "1366505460297",
//            "id": "15005",
//            "incoming": "false",
//            "threadKey": null
//        },
//        {
//            "address": "+16823679168",
//            "body": "Now\n",
//            "date": "1366505374270",
//            "id": "15004",
//            "incoming": "true",
//            "threadKey": null
//        },
//        {
//            "address": "+16823679168",
//            "body": "good good, chung` nao` nau' an?",
//            "date": "1366505347685",
//            "id": "14007",
//            "incoming": "false",
//            "threadKey": null
//        }
    ];
    $scope.threads = [
//        {
//            "address": "+12052085117",
//            "date": "1366450027708",
//            "id": "+12052085117118226533603167233005",
//            "userId": "118226533603167233005"
//        },
//        {
//            "address": "+16823679168",
//            "date": "1366485584711",
//            "id": "+16823679168118226533603167233005",
//            "userId": "118226533603167233005"
//        }
    ];

    $scope.enjoyApp = function () {
        $scope.loading = true;
        $.when($scope.fetchConversations(),
                $scope.fetchCurrentConversation())
            .done(function () {
                $('#authOps').show('slow');
                helper.people();
                $scope.loading = false;
            });
    };

    $scope.exitApp = function () {
        $('#main').empty();
        $('#visiblePeople').empty();
    };

    $scope.addMessage = function ($event, messageBody) {
        $event.preventDefault();

        var date = new Date().getTime();

        var jsonObj = {
            userId: $scope.userProfile.id,
            address: $scope.currentConversationAddress,
            body: messageBody,
            incoming: false,
            date: date
        };

        console.log('jsonObj: ', jsonObj);


        ThumbsyApi.addMessage(jsonObj)
            .success(function (respData, respStatus, headers, config) {
                $scope.respData = respData;
                $scope.respStatus = respStatus;

                // NOTE: push temporarily
                // confirm when received notice from android client
                // if didn't send, should delete
                // and then set input textbox to the message
                $scope.messages.push(jsonObj);
                $scope.messageBody = '';
                $("#message-box").animate({ scrollTop: $('#message-box').prop("scrollHeight")}, 1000);

            }).error(function (respData, respStatus, headers, config) {
                $scope.respData = respData;
                $scope.respStatus = respStatus;
                alert("Error sending message!!!\n" + respStatus);
            });

        $event.preventDefault();
    };

    $scope.fetchCurrentConversation = function () {
        ThumbsyApi.getCurrentMessages($scope.userProfile.id)
            .success(function (data, status) {
                console.log('ThumbsyApi.getCurrentMessages status: ', status)

                if ($.isArray(data.sms))
                    $scope.messages = data.sms;
                else
                    $scope.messages.push(data.sms);

                $scope.currentConversationAddress = $scope.messages[0].address;
                $("#message-box").animate({ scrollTop: $('#message-box').prop("scrollHeight")}, 1000);
            }).error(function (data, status) {
                console.log('error ThumbsyApi.getCurrentMessages: ', status);
            });
    };

    $scope.fetchConversation = function (index) {
        var address = $scope.threads[index].address;
        $scope.currentConversationAddress = address;

        // reset current messages array
        $scope.messages = [];

        console.log('fetchConversation' + address, true);

        ThumbsyApi.getMessages($scope.userProfile.id, address)
            .success(function (data, status) {
                console.log('ThumbsyApi.getMessages status: ', status);

                if ($.isArray(data.sms))
                    $scope.messages = data.sms;
                else
                    $scope.messages.push(data.sms);

                $("#message-box").animate({ scrollTop: $('#message-box').prop("scrollHeight")}, 1000);

            }).error(function (data, status) {
                console.log('error ThumbsyApi.getMessages: ', status);
            });
    };

    $scope.fetchConversations = function () {
        ThumbsyApi.getConversations($scope.userProfile.id)
            .success(function (data, status) {

                if ($.isArray(data.smsThread))
                    $scope.threads = data.smsThread;
                else
                    $scope.threads.push(data.smsThread);

            })
            .error(function (data, status) {
                console.log('error ThumbsyApi.getConversations: ', status);
            });
    };

    $scope.getTotalMessages = function () {
        return $scope.messages.length;
    };

    $scope.getCharCount = function (messageBody) {
        return MAX_CHAR_PER_TEXT - ("" + messageBody).length;
    };

    $scope.deleteMessage = function (index) {
        $scope.messages.splice(index, 1);
    };


    $scope.onSocketMessage = function (message) {

        var messageXML = ((new DOMParser()).parseFromString(message.data, "text/xml"));
        var messageType = messageXML.documentElement.getElementsByTagName("type")[0].firstChild.nodeValue;
        if (messageType == "updateFriendList") {
//            addToFriends(messageXML.documentElement.getElementsByTagName("message")[0].firstChild.nodeValue);
        } else if (messageType == "updateChatBox") {
            var address = messageXML.documentElement.getElementsByTagName("address")[0].firstChild.nodeValue;

            if (address == $scope.currentConversationAddress)
                $scope.fetchCurrentConversation();
            else
                alert('you got new message from: ' + address);
        }
    };

    $scope.onSocketError = function (error) {
        alert("Error is <br/>" + error.description + " <br /> and HTML code" + error.code);
    };

    $scope.onSocketOpen = function () {
        // socket opened
        console.log('onSocketOpen: ', true);
    };

    $scope.onSocketClose = function () {
        alert("Socket Connection closed");
    };

    $scope.openChannel = function (token) {
        var channel = new goog.appengine.Channel(token);
        var socket = channel.open();
        socket.onopen = $scope.onSocketOpen;
        socket.onmessage = $scope.onSocketMessage;
        socket.onerror = $scope.onSocketError;
        socket.onclose = $scope.onSocketClose;
    };

    $scope.requestChannelToken = function (userId) {
        var getTokenURI = '/gettoken?googleUserId=' + userId;
        var httpRequest = ThumbsyApi.makeRequestToken(getTokenURI, true);

        httpRequest.onreadystatechange = function () {
            if (httpRequest.readyState === 4) {
                if (httpRequest.status === 200) {
                    $scope.openChannel(httpRequest.responseText);
                } else {
                    alert('There was a problem with the request.');
                }
            }
        }
    };

    /**
     * Calls the OAuth2 endpoint to disconnect the app for the user.
     */
    $scope.disconnect = function () {

        // Revoke the access token.
        $.ajax({
            type: 'GET',
            url: 'https://accounts.google.com/o/oauth2/revoke?token=' +
                gapi.auth.getToken().access_token,
            async: false,
            contentType: 'application/json',
            dataType: 'jsonp',
            success: function (result) {
                console.log('revoke response: ' + result);

                $('#authOps').hide();
                $('#profile').empty();
                $('#visiblePeople').empty();
                $('#authResult').empty();
                $('#gConnect').show();

                $scope.$apply(function () {
                    $scope.signedOut();
                });

            },
            error: function (e) {
                console.log(e);
            }
        });
    };

    $scope.removePhotoFromArray = function (array, photoId) {
        var newArray = [];
        angular.forEach(array, function (value, key) {
            if (value.id != photoId) {
                newArray.push(value);
            }
        });
        return newArray;
    };

    $scope.signedOut = function () {
        $scope.userProfile = undefined;
        $scope.hasUserProfile = false;
        $scope.isSignedIn = false;
        $scope.immediateFailed = true;
        $scope.userPhotos = [];
        $scope.friendsPhotos = [];
        //$scope.renderSignIn();

        $scope.exitApp();
    };

    $scope.signedIn = function (profile) {
        $scope.isSignedIn = true;
        $scope.userProfile = profile;
        $scope.hasUserProfile = true;

        console.log('ID: ' + profile.id);
        console.log('Display Name: ' + profile.displayName);
        console.log('Image URL: ' + profile.image.url);
        console.log('Profile URL: ' + profile.url);
    };

    /**
     * Calls the helper method that handles the authentication flow.
     *
     * @param {Object} authResult An Object which contains the access token and
     *   other authentication information.
     */
    $scope.onSignInCallback = function (authResult) {

        console.log('called onSignInCallback: ' + true);

        $scope.$apply(function () {
            $scope.processAuth(authResult);
        });
    };

    $scope.processAuth = function (authResult) {
        console.log('called processAuth: ' + true);

        // show button
        $scope.immediateFailed = true;
        if ($scope.isSignedIn) {
            return;
        }

        gapi.client.load('plus', 'v1', function () {
            $('#authResult').html('Auth Result:<br/>');
            for (var field in authResult) {
                $('#authResult').append(' ' + field + ': ' +
                    authResult[field] + '<br/>');
            }

            if (authResult['access_token']) {
                console.log('signed in: ' + true);

                $scope.immediateFailed = false;

                // request to get user google info
                var request = gapi.client.plus.people.get({
                    'userId': 'me'
                });
                request.execute(function (resp) {
                    $scope.$apply(function () {
                        $scope.signedIn(resp);

                        $scope.requestChannelToken($scope.userProfile.id);

                        $scope.enjoyApp();
                    });
                });
            } else if (authResult['error']) {
                console.log('authResult[\'error\']: ' + authResult['error']);
                // There was an error, which means the user is not signed in.
                if (authResult['error'] == 'immediate_failed') {
                    // show signin button
                    $scope.immediateFailed = true;
                    $('#authOps').hide('slow');
                } else {
                    // other error
                    console.log('There was an error: ' + authResult['error']);
                }
            }
            console.log('authResult', authResult);
        });

    };

    $scope.renderSignIn = function () {
        console.log('called renderSignin: ' + true);
        gapi.signin.render('myGsignin', {
            'callback': $scope.onSignInCallback,
            'clientid': Conf.clientId,
            'requestvisibleactions': Conf.requestvisibleactions,
            'scope': Conf.scopes,
            // Remove the comment below if you have configured
            // appackagename in services.js
            //'apppackagename': Conf.apppackagename,
            'theme': 'dark',
            'cookiepolicy': Conf.cookiepolicy,
            'width': 'wide'
//            'accesstype': 'offline'
        });
    };

    $scope.start = function () {
        $scope.renderSignIn();

//        $scope.checkForHighlightedPhoto();
//        PhotoHuntApi.getThemes().then(function (response) {
//            $scope.themes = response.data;
//            $scope.selectedTheme = $scope.themes[0];
//            $scope.orderBy('recent');
//            $scope.getUserPhotos();
//            var options = {
//                'clientid': Conf.clientId,
//                'contenturl': Conf.rootUrl + '/invite.html',
//                'contentdeeplinkid': '/',
//                'prefilltext': 'Join the hunt, upload and vote for photos of ' +
//                    $scope.selectedTheme.displayName + '. #photohunt',
//                'calltoactionlabel': 'Join',
//                'calltoactionurl': Conf.rootUrl,
//                'calltoactiondeeplinkid': '/',
//                'callback': $scope.signIn,
//                'requestvisibleactions': Conf.requestvisibleactions,
//                'scope': Conf.scopes,
//                'cookiepolicy': Conf.cookiepolicy
//            };
//            gapi.interactivepost.render('invite', options);
//            $scope.getAllPhotos();
//        });
    };

    console.log('start: ' + true);
    $scope.start();
}