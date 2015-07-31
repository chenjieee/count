var countApp = angular.module('countApp');

countApp.controller('countController', function($scope, $http, growl, $modal) {

    $scope.now = '(N/A)';

    $http.get('api/now').success(function(data) {
        $scope.now = data || '(N/A)';
    });

});
