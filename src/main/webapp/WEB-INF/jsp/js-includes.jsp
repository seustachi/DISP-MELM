<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
  var melmContextRoot = "${ctx}";
</script>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/angular.min.js"></script>
<script type="text/javascript" src="${ctx}/js/angular-sanitize.min.js"></script>
<script type="text/javascript" src="${ctx}/js/angular-translate.min.js"></script>
<script type="text/javascript" src="${ctx}/js/ui-bootstrap-tpls-0.11.0.min.js"></script>
<script type="text/javascript" src="${ctx}/js/dialogs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/xeditable.min.js"></script>
<script type="text/javascript" src="${ctx}/js/es5-shim.min.js"></script>
<script type="text/javascript" src="${ctx}/js/angular-file-upload.min.js"></script>
<script type="text/javascript">
  var app = angular.module('app', [ 'xeditable', 'ui.bootstrap', 'angularFileUpload', 'dialogs.main', 'pascalprecht.translate']);
</script>
<script type="text/javascript" src="${ctx}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/js/custom/functions.js"></script>
<script type="text/javascript" src="${ctx}/js/custom/filters.js"></script>
<script type="text/javascript" src="${ctx}/js/custom/directives.js"></script>