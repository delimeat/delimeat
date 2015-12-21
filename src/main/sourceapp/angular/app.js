var app = angular.module('app', [
'ngRoute',
'ngResource',
'ngAnimate',
'ngMaterial',
'delimeat.shared',
'delimeat.shows',
'delimeat.settings',
'delimeat.guide',
'delimeat.show'
]);


app.config(['$mdIconProvider', function($mdIconProvider) {
    $mdIconProvider
        .iconSet('action', 'resources/iconsets/action-icons.svg', 24)
        .iconSet('alert', 'resources/iconsets/alert-icons.svg', 24)
        .iconSet('av', 'resources/iconsets/av-icons.svg', 24)
        .iconSet('communication', 'resources/iconsets/communication-icons.svg', 24)
        .iconSet('content', 'resources/iconsets/content-icons.svg', 24)
        .iconSet('device', 'resources/iconsets/device-icons.svg', 24)
        .iconSet('editor', 'resources/iconsets/editor-icons.svg', 24)
        .iconSet('file', 'resources/iconsets/file-icons.svg', 24)
        .iconSet('hardware', 'resources/iconsets/hardware-icons.svg', 24)
        .iconSet('icons', 'resources/iconsets/icons-icons.svg', 24)
        .iconSet('image', 'resources/iconsets/image-icons.svg', 24)
        .iconSet('maps', 'resources/iconsets/maps-icons.svg', 24)
        .iconSet('navigation', 'resources/iconsets/navigation-icons.svg', 24)
        .iconSet('notification', 'resources/iconsets/notification-icons.svg', 24)
        .iconSet('social', 'resources/iconsets/social-icons.svg', 24)
        .iconSet('toggle', 'resources/iconsets/toggle-icons.svg', 24);
}]);
