<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>Pulse Collection - Add library element</title>
<link rel="icon" href="${ctx}/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/addLibraryIcon.js"></script>
<script type="text/javascript">
  var libraryId = parseInt("${it.library.id}", 10);
</script>
</head>
<body ng-controller="AddLibraryIconCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>
        Add library element <small>${it.library.name}-${it.library.majorVersion}.${it.library.minorVersion}</small>
      </h1>
    </div>
    <progressbar class="progress-striped active" value="100" type="warning" ng-show="loadingVisible"> <i>Loading resources</i></progressbar>
    <c:if test="${not empty it.error}">
      <div class="alert alert-danger">${it.error}</div>
    </c:if>
    <form name="addLibraryIconForm" method="POST" action="${ctx}/rest/libraries/icons/add" class="form-horizontal" role="form">
      <input name="id" id="id" type="hidden" value="${it.library.id}" /> <input name="iconIndex" id="iconIndex" type="hidden" value="-1" />
      <input type="text" id="iconId" name="iconId" ng-value="id" ng-checked="isSelected(-1)" style="display: none" />
      <div class="row">
        <div ng-click="selectImage(resource.icon.id, resource.libraries)" ng-class="getClasses(resource.icon.id, resource.libraries)"
          ng-repeat="resource in resources | startFrom: (currentPage - 1) * itemsPerPage | limitTo: itemsPerPage">
          <img ng-src="${ctx}/rest/icons/file/{{resource.icon.id}}/MEDIUM" alt="{{resource.icon.displayName}}" /><br />{{resource.icon.displayName}}
        </div>
      </div>
      <div class="pagination-centered">
        <pagination total-items="totalItems" ng-model="currentPage" max-size="5" class="pagination-sm" boundary-links="true" rotate="false"
          items-per-page="itemsPerPage"></pagination>
      </div>
      <div class="form-group">
        <label for="iconName" class="col-sm-2 control-label">Element name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconName" name="iconName" placeholder="Specify an element name" ng-model="iconName" ng-minlength="3" ng-maxlength="40" ng-required="true"/> <span class="error"
              ng-show="addLibraryIconForm.iconName.$error.minlength"> Too short (3-40)!</span> <span class="error"
              ng-show="addLibraryIconForm.iconName.$error.maxlength"> Too long (3-40)!</span>
        </div>
      </div>
      <div class="form-group">
        <label for="iconDescription" class="col-sm-2 control-label">Element description</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconDescription" name="iconDescription" placeholder="Specify an element description" ng-model="iconDescription" ng-minlength="3" ng-maxlength="40" ng-required="true"/> <span class="error"
              ng-show="addLibraryIconForm.iconDescription.$error.minlength"> Too short (3-40)!</span> <span class="error"
              ng-show="addLibraryIconForm.iconDescription.$error.maxlength"> Too long (3-40)!</span>
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn btn-add" ng-disabled="!isAddActive()">Add</button>
          <button type="button" ng-click="back()" class="btn btn-default">
            <span class="glyphicon glyphicon-remove"></span>Cancel
          </button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>
