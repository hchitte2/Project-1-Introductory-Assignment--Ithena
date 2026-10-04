<c:set var="pageTitle" value="Log in"/>
<!doctype html>
<html lang="en">
<head>
  <%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body class="auth">
  <%@ include file="/WEB-INF/jspf/brand.jspf" %>

  <main class="auth-main">
    <div class="auth-card">
      <h1>Log in</h1>

      <c:if test="${param.loggedOut != null}">
        <p class="notice success" role="status">You've logged out.</p>
      </c:if>
      <c:if test="${param.reset != null}">
        <p class="notice success" role="status">Your password has been reset. Log in with your new password.</p>
      </c:if>
      <c:if test="${param.required != null}">
        <p class="notice info" role="status">Log in to see that page.</p>
      </c:if>
      <c:if test="${not empty error}">
        <p class="notice error" role="alert">${fn:escapeXml(error)}</p>
      </c:if>

      <form method="post" action="${ctx}/login" novalidate>
        <ui:field name="userId" label="User ID" value="${userId}" autocomplete="username" autofocus="true"/>

        <div class="field">
          <div class="label-row">
            <label for="password">Password</label>
            <a href="${ctx}/forgot-password">Forgot password?</a>
          </div>
          <input id="password" name="password" type="password" autocomplete="current-password" required>
        </div>

        <button class="btn-primary" type="submit">Log in</button>
      </form>

      <p class="switch">New here? <a href="${ctx}/register">Create an account</a></p>
    </div>
  </main>
</body>
</html>
