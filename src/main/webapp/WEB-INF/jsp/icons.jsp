<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MALM - List Icons</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/icons.js"></script>
</head>
<body ng-controller="IconsCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>List Icons</h1>
    </div>
    <progressbar class="progress-striped active" value="100" type="warning" ng-show="loadingVisible">
    <i>Loading resources</i></progressbar>
    <div class="alert alert-danger" ng-show="error!=null">{{error}}</div>
    <div class="table-responsive">
      <table class="table table-striped">
        <tr>
          <td align="left" style="width: 200px"><a href="" ng-click="predicate='icon.displayName'; reverse=!reverse">Name</a></td>
          <td align="left" style="width: 200px"><a href="" ng-click="predicate='icon.anchor'; reverse=!reverse">Anchor</a></td>
          <td align="left" style="width: 200px">Linked libraries</td>
          <td align="left" style="width: 100px">Preview</td>
          <td align="left">Actions</td>
        </tr>
        <tr ng-repeat="icon in icons | orderBy:predicate:reverse | startFrom: (currentPage - 1) * itemsPerPage | limitTo: itemsPerPage">
          <td align="left">{{icon.icon.displayName}}</td>
          <td align="left">{{icon.icon.anchor}}</td>
          <td align="left"><div ng-repeat="library in icon.libraries">
              <a href="" ng-click="go('/rest/libraries/icons/'+library.id)">{{library.name}}</a>
            </div></td>
          <td align="left"><a href="${ctx}/rest/icons/details/{{icon.icon.id}}"><img
              src="${ctx}/rest/icons/file/{{icon.icon.id}}/MEDIUM"></a></td>
          <td align="left">
            <ul class="nav nav-pills">
              <li><button class="btn" ng-click="go('/rest/icons/update/'+icon.icon.id)">
                  <span class="glyphicon glyphicon-refresh"></span><span class="hidden-xs hidden-sm">Update</span>
                </button></li>
              <li><button class="btn" ng-disabled="hasLibraries(icon)" ng-click="confirmDelete(icon.icon.id)">
                  <span class="glyphicon glyphicon-remove"></span>Delete
                </button></li>
            </ul>
          </td>
        </tr>
      </table>
    </div>
    <div class="pagination-centered">
      <pagination total-items="totalItems" ng-model="currentPage" max-size="5" class="pagination-sm" boundary-links="true" rotate="false"
        items-per-page="itemsPerPage"></pagination>
    </div>
    <hr />
    <button class="btn btn-add" ng-click="go('/rest/icons/add')">Add</button>
  </div>
  <hr />
  <jsp:include page="footer.jsp" />
</body>
</html>
