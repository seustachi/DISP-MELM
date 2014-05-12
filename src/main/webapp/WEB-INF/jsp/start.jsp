<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MELM</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script src="${ctx}/js/custom/start.js"></script>
</head>
<body ng-controller="StartCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>MELM</h1>
    </div>
    <h2>Libraries</h2>
    <ul class="nav nav-pills">
      <li><button class="btn btn-primary" ng-click="go('/rest/libraries')">
          <span class="glyphicon glyphicon-th-list"></span>  List</a></button></li>
      <li><button class="btn btn-info" ng-click="go('/rest/libraries/import')">
          <span class="glyphicon glyphicon-cloud-upload"></span>  Import</a></button></li>
      <li><button class="btn btn-info" ng-click="go('/rest/libraries/add')">
          <span class="glyphicon glyphicon-plus"></span>  Add</a></button></li>
    </ul>
    <h2>Icons</h2>
    <ul class="nav nav-pills">
      <li><button class="btn btn-primary" ng-click="go('/rest/icons')">
          <span class="glyphicon glyphicon-th-list"></span>  List</a></button></li>
      <li><button class="btn btn-info" ng-click="go('/rest/icons/add')">
          <span class="glyphicon glyphicon-plus"></span>  Add</a></button></li>
    </ul>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>