/*
 javascript file for thumbsy app

 */
'use strict';

var DEBUG = true;
var DEBUG_WEB = true;

var MAX_CHAR_PER_TEXT = 500;

var postMessageUrl = "http://thumbsy-demo.appspot.com/sendAll";
var fetchConversationUrl = "http://thumbsy-demo.appspot.com/rest/messages/conversation/";

if (DEBUG) {
    postMessageUrl = "http://localhost:8888/sendAll";
    fetchConversationUrl = "http://localhost:8888/rest/messages/conversation/";
}

var currentConvId = 1;


function ConversationCtrl($scope, $http) {

    // retrieve global data from different controllers
//    $scope.data = Data;

    // array of messages for this conversation
    $scope.messages = [
        {content: 'blah blah', incoming: false},
        {content: 'umbala', incoming: true},
        {content: 'blah blah', incoming: false},
        {content: 'umbala', incoming: true},
        {content: 'blah blah', incoming: false},
        {content: 'umbala', incoming: true},
        {content: 'blah blah', incoming: false},
        {content: 'umbala', incoming: true},
        {content: 'blah blah', incoming: false},
        {content: 'umbala', incoming: true},
        {content: 'blah blah', incoming: false},
        {content: 'umbala', incoming: true}
    ];


    $scope.addMessage = function (messageContent) {

        var convId = 1;
        var msgId = $scope.messages.length + 1;


        var jsonObj = {id: msgId, conversationId: convId, content: messageContent, incoming: false};

        $scope.messages.push(jsonObj);
        $scope.messageContent = '';
//        $('message-box').scrollTop($('message-box').prop("scrollHeight"));
        $("message-box").prop("scrollHeight");

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
//                $("message-box").prop("scrollHeight");

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

}

function ThumbsyCtrl($scope, $location, Conf, PhotoHuntApi) {
    // signIn
    $scope.userProfile = undefined;
    $scope.hasUserProfile = false;
    $scope.isSignedIn = false;
    $scope.immediateFailed = false;
    // themes
    $scope.selectedTheme;
    $scope.themes = [];
    // photos
    $scope.ordering;
    $scope.recentButtonClasses;
    $scope.popularButtonClasses;
    $scope.highlightedPhoto;
    $scope.userPhotos = [];
    $scope.friendsPhotos = [];
    $scope.allPhotos = [];
    // friends
    $scope.friends = [];
    // uploads
    $scope.uploadUrl;

    $scope.disconnect = function () {
        PhotoHuntApi.disconnect().then(function () {
            $scope.userProfile = undefined;
            $scope.hasUserProfile = false;
            $scope.isSignedIn = false;
            $scope.immediateFailed = true;
            $scope.userPhotos = [];
            $scope.friendsPhotos = [];
            //$scope.renderSignIn();
        });
    }

    // methods
    $scope.orderBy = function (criteria) {
        var activeItemClasses = ['active', 'primary'];
        if (criteria == 'recent') {
            $scope.ordering = '-dateCreated';
            $scope.recentButtonClasses = activeItemClasses;
            $scope.popularButtonClasses = [];
        } else if (criteria == 'popular') {
            $scope.ordering = '-votes';
            $scope.popularButtonClasses = activeItemClasses;
            $scope.recentButtonClasses = [];
        } else {
            return 0;
        }
    };

    $scope.adaptPhotos = function (photos) {
        angular.forEach(photos, function (value, key) {
            value['canDelete'] = false;
            value['canVote'] = false;
            value['voteClass'] = [];
            if ($scope.hasUserProfile) {
                if (value.ownerUserId == $scope.userProfile.id) {
                    value['canDelete'] = true;
                } else {
                    if ($scope.userProfile.role == 'admin') {
                        value['canDelete'] = true;
                    }
                    value['canVote'] = true;
                    value['voteClass'] = ['button', 'icon', 'arrowup'];
                    if (value.voted) {
                        value['voteClass'].push('disable');
                    } else {
                        value.voted = false;
                    }
                }
            }
        });
        return photos;
    }

    $scope.deletePhoto = function (photoId) {
        PhotoHuntApi.deletePhoto(photoId);
        $scope.userPhotos = $scope.removePhotoFromArray($scope.userPhotos, photoId);
        $scope.friendsPhotos = $scope.removePhotoFromArray($scope.friendsPhotos, photoId);
        $scope.allPhotos = $scope.removePhotoFromArray($scope.allPhotos, photoId);
    }

    $scope.removePhotoFromArray = function (array, photoId) {
        var newArray = [];
        angular.forEach(array, function (value, key) {
            if (value.id != photoId) {
                newArray.push(value);
            }
        });
        return newArray;
    }

    $scope.getUserPhotos = function () {
        if ($scope.hasUserProfile && ($scope.themes.length > 0)) {
            PhotoHuntApi.getUserPhotosByTheme($scope.selectedTheme.id)
                .then(function (response) {
                    $scope.userPhotos = $scope.adaptPhotos(response.data);
                });
        }
    }

    $scope.getAllPhotos = function () {
        PhotoHuntApi.getAllPhotosByTheme($scope.selectedTheme.id)
            .then(function (response) {
                $scope.allPhotos = $scope.adaptPhotos(response.data);
            });
    }

    $scope.getFriendsPhotos = function () {
        PhotoHuntApi.getFriendsPhotosByTheme($scope.selectedTheme.id)
            .then(function (response) {
                $scope.friendsPhotos = $scope.adaptPhotos(response.data);
            });
    }

    $scope.getUploadUrl = function (params) {
        PhotoHuntApi.getUploadUrl().then(function (response) {
            $scope.uploadUrl = response.data;
        });
    }

    $scope.checkIfVoteActionRequested = function () {
        if ($location.search()['action'] == 'VOTE') {
            PhotoHuntApi.votePhoto($location.search()['photoId'])
                .then(function (response) {
                    var photo = response.data;
                    $scope.highlightedPhoto = photo;
                    $scope.notification = 'Thanks for voting!';
                });
        }
    }

    $scope.getFriends = function () {
        PhotoHuntApi.getFriends().then(function (response) {
            $scope.friends = response.data;
            $scope.getFriendsPhotos();
        })
    }

    $scope.selectTheme = function (themeIndex) {
        $scope.selectedTheme = $scope.themes[themeIndex];
        if ($scope.selectedTheme.id != $scope.themes[0].id) {
            $scope.orderBy('popular');
        }
        $scope.getAllPhotos();
        if ($scope.isSignedIn) {
            $scope.getUserPhotos();
        }
        if ($scope.friends.length) {
            $scope.getFriendsPhotos();
        }
    }

    $scope.canUpload = function () {
        if ($scope.uploadUrl)
            return true;
        else
            return false;
    }

    $scope.uploadedPhoto = function (uploadedPhoto) {
        uploadedPhoto['canDelete'] = true;
        $scope.userPhotos.unshift(uploadedPhoto);
        $scope.allPhotos.unshift(uploadedPhoto);
        $scope.getUploadUrl();
    }

    $scope.signedIn = function (profile) {
        $scope.isSignedIn = true;
        $scope.userProfile = profile;
        $scope.hasUserProfile = true;
        $scope.getUserPhotos();
        // refresh the state of operations that depend on the local user
        $scope.allPhotos = $scope.adaptPhotos($scope.allPhotos);
        // now we can perform other actions that need the user to be signed in
        $scope.getUploadUrl();
        $scope.checkIfVoteActionRequested();
        $scope.getFriends();
    };

    $scope.checkForHighlightedPhoto = function () {
        if ($location.search()['photoId']) {
            PhotoHuntApi.getPhoto($location.search()['photoId'])
                .then(function (response) {
                    $scope.highlightedPhoto = response.data;
                })
        }
    }

    $scope.signIn = function (authResult) {
        $scope.$apply(function () {
            $scope.processAuth(authResult);
        });
    }

    $scope.processAuth = function (authResult) {
        $scope.immediateFailed = true;
        if ($scope.isSignedIn) {
            return 0;
        }
        if (authResult['access_token']) {
            $scope.immediateFailed = false;
            // Successfully authorized, create session
            PhotoHuntApi.signIn(authResult).then(function (response) {
                $scope.signedIn(response.data);
            });
        } else if (authResult['error']) {
            if (authResult['error'] == 'immediate_failed') {
                $scope.immediateFailed = true;
            } else {
                console.log('Error:' + authResult['error']);
            }
        }
    }

    $scope.renderSignIn = function () {
        gapi.signin.render('myGsignin', {
            'callback': $scope.signIn,
            'clientid': Conf.clientId,
            'requestvisibleactions': Conf.requestvisibleactions,
            'scope': Conf.scopes,
            // Remove the comment below if you have configured
            // appackagename in services.js
            //'apppackagename': Conf.apppackagename,
            'theme': 'dark',
            'cookiepolicy': Conf.cookiepolicy,
            'accesstype': 'offline'
        });
    }

    $scope.start = function () {
        $scope.renderSignIn();
        $scope.checkForHighlightedPhoto();
        PhotoHuntApi.getThemes().then(function (response) {
            $scope.themes = response.data;
            $scope.selectedTheme = $scope.themes[0];
            $scope.orderBy('recent');
            $scope.getUserPhotos();
            var options = {
                'clientid': Conf.clientId,
                'contenturl': Conf.rootUrl + '/invite.html',
                'contentdeeplinkid': '/',
                'prefilltext': 'Join the hunt, upload and vote for photos of ' +
                    $scope.selectedTheme.displayName + '. #photohunt',
                'calltoactionlabel': 'Join',
                'calltoactionurl': Conf.rootUrl,
                'calltoactiondeeplinkid': '/',
                'callback': $scope.signIn,
                'requestvisibleactions': Conf.requestvisibleactions,
                'scope': Conf.scopes,
                'cookiepolicy': Conf.cookiepolicy
            };
            gapi.interactivepost.render('invite', options);
            $scope.getAllPhotos();
        });
    }

    $scope.start();

}

$(document).ready(function () {
});


