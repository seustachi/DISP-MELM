<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<html>
<head>
<title>Start page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
</head>
<body>
  <div id="wrapper">
    <jsp:include page="header.jsp" />
    <div id="content">
      <h1>Start page</h1>
      <h2>Libraries</h2>
      <a href="${ctx}/rest/libraries">List</a>
      <br/>
      <a href="${ctx}/rest/libraries/import">Import</a>
      <h2>Icons</h2>
      <a href="${ctx}/rest/icons">List</a>
      <br/>
      <a href="${ctx}/rest/icons/add">Add</a>
    </div>
    <jsp:include page="footer.jsp" />
  </div>
</body>
</html>