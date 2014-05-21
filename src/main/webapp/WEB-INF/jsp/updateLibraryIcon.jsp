<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MALM - Update library element</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript">
  var iconId = parseInt("${it.libraryIcon.icon.id}", 10);
  var libraryId = parseInt("${it.libraryIcon.library.id}", 10);
</script>
<script type="text/javascript" src="${ctx}/js/custom/updateLibraryIcon.js"></script>
</head>
<body ng-controller="UpdateLibraryIconCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>
        Update library element <small>${it.libraryIcon.library.name}-${it.libraryIcon.library.majorVersion}.${it.libraryIcon.library.minorVersion}</small>
      </h1>
    </div>
    <c:if test="${not empty it.error}">
      <div class="alert alert-danger">${it.error}</div>
    </c:if>
    <form method="POST" action="${ctx}/rest/libraries/icons/update" class="form-horizontal" role="form">
      <input name="libraryIconId" id="libraryIconId" type="hidden" value="${it.libraryIcon.id}" /> <input name="id" id="id" type="hidden"
        value="${it.libraryIcon.library.id}" /><input type="text" id="iconId" name="iconId" ng-value="iconId" style="display: none" />
      <div class="row">
        <div ng-click="selectImage(icon.icon.id, icon.libraries)" ng-class="getClasses(icon.icon.id, icon.libraries)"
          ng-repeat="icon in icons | startFrom: pagination.page * pagination.perPage | limitTo: pagination.perPage">
          <img src="${ctx}/rest/icons/file/{{icon.icon.id}}/MEDIUM" alt="{{icon.icon.displayName}}" /><br />{{icon.icon.displayName}}
        </div>
      </div>
      <div class="pagination-centered">
        <ul class="pagination">
          <li><a ng-hide="pagination.page == 0" ng-click="pagination.prevPage()">&laquo;</a></li>
          <li ng-repeat="n in [] | range: pagination.numPages" ng-class="{current: n == pagination.page}"><a
            ng-click="pagination.toPageId(n)">{{n + 1}}</a></li>
          <li><a ng-hide="pagination.page + 1 >= pagination.numPages" ng-click="pagination.nextPage()">&raquo;</a></li>
        </ul>
      </div>
      <div class="form-group">
        <label for="iconIndex" class="col-sm-2 control-label">Element index</label>
        <div class="col-sm-10">
          <input type="number" class="form-control" id="iconIndex" name="iconIndex" placeholder="Specify an element index"
            value="${it.libraryIcon.indexOfIconInLibrary}" />
        </div>
      </div>
      <div class="form-group">
        <label for="iconName" class="col-sm-2 control-label">Element name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconName" name="iconName" placeholder="Specify an element name"
            value="${it.libraryIcon.iconNameInLibrary}" />
        </div>
      </div>
      <div class="form-group">
        <label for="iconDescription" class="col-sm-2 control-label">Element description</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconDescription" name="iconDescription" placeholder="Specify an element description"
            value="${it.libraryIcon.iconDescriptionInLibrary}" />
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn">
            <span class="glyphicon glyphicon-refresh"></span>Update
          </button>
          <button type="button" ng-click="back()" class="btn btn-default btn-custom-cancel">
            <span class="glyphicon glyphicon glyphicon-step-backward"></span><span class="hidden-xs hidden-sm">Back</span>
          </button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>