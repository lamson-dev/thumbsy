'use strict';

var appServices = angular.module('myApp.services', []);

appServices.factory('Conf', function ($location) {
    function getRootUrl() {
        var rootUrl = $location.protocol() + '://' + $location.host();
        if ($location.port())
            rootUrl += ':' + $location.port();
        return rootUrl;
    }

    return {
        'clientId': '1057121896515-f47mlri6s9201thcnrk42hbrgki8hhhs.apps.googleusercontent.com',
        'apiBase': '/api/',
        'rootUrl': getRootUrl(),
        'scopes': 'https://www.googleapis.com/auth/plus.login ',
        'requestvisibleactions': 'http://schemas.google.com/AddActivity ' +
            'http://schemas.google.com/ReviewActivity',
        'cookiepolicy': 'single_host_origin',
        'width': 'wide'
        // If you have an android application and you want to enable
        // Over-The-Air install, remove the comment below and use the package
        // name associated to the project's Client Id for installed applications
        // in the Google API Console.
        //'apppackagename': 'YOUR_APP_PACKAGE'
    };
});

var debugUrl = "http://thumbsy-demo.appspot.com/api/";

appServices.factory('ThumbsyApi', function ($http, Conf) {
    return {

        getConversations: function (userId) {
            return $http.get(Conf.apiBase + "conversations/" + userId);
        },

        getCurrentMessages: function (userId) {
            return $http.get(Conf.apiBase + 'messages/conversation/current/' + userId);
        },

        getMessages: function (userId, phoneAddress) {
            return $http.get(Conf.apiBase + "messages/conversation/" + phoneAddress + '/' + userId);
        },

        addMessage: function (jsonMsgData) {
            return $http.post('/sendAll', jsonMsgData);
        },

        signIn: function (authResult) {
            return $http.post(Conf.apiBase + 'connect', authResult);
        },
        votePhoto: function (photoId) {
            return $http.put(Conf.apiBase + 'votes',
                {'photoId': photoId});
        },
        getThemes: function () {
            return $http.get(Conf.apiBase + 'themes');
        },
        getUploadUrl: function () {
            return $http.post(Conf.apiBase + 'images');
        },
        getAllPhotosByTheme: function (themeId) {
            return $http.get(Conf.apiBase + 'photos',
                {params: {'themeId': themeId}});
        },
        getPhoto: function (photoId) {
            return $http.get(Conf.apiBase + 'photos', {params: {'photoId': photoId}});
        },
        getUserPhotosByTheme: function (themeId) {
            return $http.get(Conf.apiBase + 'photos', {params: {'themeId': themeId, 'userId': 'me'}});
        },
        getFriends: function () {
            return $http.get(Conf.apiBase + 'friends');
        },
        getFriendsPhotosByTheme: function (themeId) {
            return $http.get(Conf.apiBase + 'photos', {params: {'themeId': themeId, 'userId': 'me', 'friends': 'true'}});
        },
        deletePhoto: function (photoId) {
            return $http.delete(Conf.apiBase + 'photos', {params: {'photoId': photoId}});
        },
        disconnect: function () {
            return $http.post(Conf.apiBase + 'disconnect');
        }
    };
})
;

myApp.factory('Data', function () {
    return {message: "I'm data from a service"};
});