<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
  var melmContextRoot = "${ctx}";
</script>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/angular.min.js"></script>
<script type="text/javascript" src="${ctx}/js/angular-animate.min.js"></script>
<script type="text/javascript" src="${ctx}/js/angular-ui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/js/ui-bootstrap-tpls-0.11.0.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap-dialog.min.js"></script>
<script type="text/javascript" src="${ctx}/js/xeditable.min.js"></script>
<script type="text/javascript" src="${ctx}/js/es5-shim.min.js"></script>
<script type="text/javascript" src="${ctx}/js/loading-bar.min.js"></script>
<script type="text/javascript" src="${ctx}/js/angular-file-upload.min.js"></script>
<script type="text/javascript">
  var app = angular.module('app', [ 'xeditable', 'ui', 'ui.bootstrap', 'angular-loading-bar', 'ngAnimate', 'angularFileUpload']);
  app.config(function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
    cfpLoadingBarProvider.latencyThreshold = 1;
    });
</script>
<script type="text/javascript" src="${ctx}/js/custom/functions.js"></script>
<script type="text/javascript" src="${ctx}/js/custom/filters.js"></script>
<script type="text/javascript" src="${ctx}/js/custom/directives.js"></script>
