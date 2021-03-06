/**
 * The ng-thumb directive
 * 
 * @author: nerv
 * @version: 0.1.2, 2014-01-09
 */
app.directive('ngThumb', [ '$window', function($window) {
  'use strict';

  var helper = {
    support : !!($window.FileReader && $window.CanvasRenderingContext2D),
    isFile : function(item) {
      return angular.isObject(item) && item instanceof $window.File;
    },
    isImage : function(file) {
      var type = '|' + file.type.slice(file.type.lastIndexOf('/') + 1) + '|';
      return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
    }
  };

  return {
    restrict : 'A',
    template : '<canvas/>',
    link : function(scope, element, attributes) {
      if (!helper.support)
        return;

      var params = scope.$eval(attributes.ngThumb);

      if (!helper.isFile(params.file))
        return;
      if (!helper.isImage(params.file))
        return;

      var canvas = element.find('canvas');
      var reader = new FileReader();

      reader.onload = onLoadFile;
      reader.readAsDataURL(params.file);

      function onLoadFile(event) {
        var img = new Image();
        img.onload = onLoadImage;
        img.src = event.target.result;
      }

      function onLoadImage() {
        /* jshint validthis:true */

        var width = params.width || this.width / this.height * params.height;
        var height = params.height || this.height / this.width * params.width;
        canvas.attr({
          width : width,
          height : height
        });
        canvas[0].getContext('2d').drawImage(this, 0, 0, width, height);
      }
    }
  };
} ]);

/**
 * Ng File Select directive
 */
app.directive("ngFileSelect", function() {
  'use strict';

  return {
    link : function($scope, el) {

      el.bind("change", function(e) {
        $scope.file = (e.srcElement || e.target).files[0];
        if ($scope.getFile) {
          $scope.getFile(e.target.id);
        }
      });
    }
  };
});