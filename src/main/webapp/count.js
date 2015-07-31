var countApp = angular.module('countApp', [ 'ngRoute', 'ngAnimate', 'ui.bootstrap', 'angular-growl' ]);

countApp.config(function($routeProvider) {

    $routeProvider.when('/', {
        templateUrl : 'views/count.html',
        controller : 'countController'
    });

});

countApp.config(function(growlProvider) {
    growlProvider.onlyUniqueMessages(false);
    growlProvider.globalDisableCountDown(true);
    growlProvider.globalTimeToLive({
        success : 5000,
        info : 5000,
        error : 10000,
        warning : 10000
    });
});

countApp.filter('offset', function() {
    return function(data, offset) {
        if (angular.isArray(data) && angular.isNumber(offset)) {
            if (offset >= 0 && offset <= data.length) {
                return data.slice(offset);
            }
        }
        return data;
    };
});
