app.controller('LibrariesCtrl', [ '$scope', '$http', function($scope, $http) {
  $scope.loadResources = function() {
    $scope.loadingVisible = true;
    $http.get(melmContextRoot + '/rest/libraries/json').success(function(data) {
      $scope.loadingVisible = false;
      $scope.libraries = data;
      $scope.totalItems = $scope.libraries.length;
    });
  };

  $scope.deleteResource = function(id) {
    if (!confirm("Do you really want to delete this resource ?")) {
      return;
    }
    var params = encodeParams({
      "id" : id
    });
    $http.post(melmContextRoot + '/rest/libraries/delete', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.loadResources();
    }).error(function() {
      alert("Resource deletion threw an error.");
    });
  };

  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

  $scope.loadingVisible = false;
  $scope.itemsPerPage = 8;
  $scope.currentPage = 1;
  $scope.predicate = 'name';
  $scope.loadResources();
} ]);