app.controller('StartCtrl', function($scope, $http) {
  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

});