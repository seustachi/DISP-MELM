<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>MALM - Add library element</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/addLibraryIcon.js"></script>
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>
        Add library element <small>${it.library.name}-${it.library.majorVersion}.${it.library.minorVersion}</small>
      </h1>
    </div>
    <form method="POST" action="${ctx}/rest/libraries/icons/add" class="form-horizontal" role="form">
      <input name="id" id="id" type="hidden" value="${it.library.id}" />
      <div class="row">
        <c:forEach var="icon" items="${it.icons}">
          <div class="col-xs-4 col-sm-2 col-md-1 icon-wrapper">
            <label class="icon" for="iconId-${icon.id}"><input type="radio" id="iconId-${icon.id}" name="iconId" value="${icon.id}"
              style="" />&nbsp;<img src="${ctx}/rest/icons/file/${icon.id}/MEDIUM"></label><br />${icon.displayName}</div>
        </c:forEach>
      </div>
      <div class="form-group">
        <label for="iconIndex" class="col-sm-2 control-label">Icon index</label>
        <div class="col-sm-10">
          <input type="number" class="form-control" id="iconIndex" name="iconIndex" placeholder="iconIndex" />
        </div>
      </div>
      <div class="form-group">
        <label for="iconName" class="col-sm-2 control-label">Icon name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconName" name="iconName" placeholder="iconName" />
        </div>
      </div>
      <div class="form-group">
        <label for="iconDescription" class="col-sm-2 control-label">Icon description</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconDescription" name="iconDescription" placeholder="iconDescription" />
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn btn-add">Add</button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>