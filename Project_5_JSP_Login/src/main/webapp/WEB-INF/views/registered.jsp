<c:set var="pageTitle" value="Account created"/>
<!doctype html>
<html lang="en">
<head>
  <%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body class="auth">
  <%@ include file="/WEB-INF/jspf/brand.jspf" %>

  <main class="auth-main">
    <div class="auth-card">
      <h1>Your account is ready</h1>
      <p class="notice success" role="status">Welcome, ${fn:escapeXml(user.firstName)}. You can now log in with your User ID and password.</p>

      <dl class="summary">
        <div><dt>Name</dt><dd>${fn:escapeXml(user.fullName)}</dd></div>
        <div><dt>User ID</dt><dd>${fn:escapeXml(user.userId)}</dd></div>
        <div><dt>Email</dt><dd>${fn:escapeXml(user.email)}</dd></div>
      </dl>

      <a class="btn-primary" href="${ctx}/login">Go to log in</a>
    </div>
  </main>
</body>
</html>
