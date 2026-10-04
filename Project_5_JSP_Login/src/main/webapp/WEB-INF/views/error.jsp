<%@ page isErrorPage="true" %>
<c:set var="status" value="${requestScope['jakarta.servlet.error.status_code']}"/>
<c:choose>
  <c:when test="${status == 404}">
    <c:set var="pageTitle" value="Page not found"/>
    <c:set var="message" value="There's no page at this address. Check the link, or start again from the portal."/>
  </c:when>
  <c:when test="${status == 405}">
    <c:set var="pageTitle" value="Action not available"/>
    <c:set var="message" value="That action can't be done from this address. Start again from the portal."/>
  </c:when>
  <c:otherwise>
    <c:set var="pageTitle" value="Something went wrong"/>
    <c:set var="message" value="The server couldn't finish your request. Try again in a moment."/>
  </c:otherwise>
</c:choose>
<!doctype html>
<html lang="en">
<head>
  <%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body class="auth">
  <%@ include file="/WEB-INF/jspf/brand.jspf" %>

  <main class="auth-main">
    <div class="auth-card">
      <p class="status-code">Error ${fn:escapeXml(status)}</p>
      <h1>${pageTitle}</h1>
      <p class="intro">${message}</p>
      <a class="btn-primary" href="${ctx}/login">Return to Member Portal</a>
    </div>
  </main>
</body>
</html>
