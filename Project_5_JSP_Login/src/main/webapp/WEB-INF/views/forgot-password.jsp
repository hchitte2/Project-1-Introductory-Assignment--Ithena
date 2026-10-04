<c:set var="pageTitle" value="Reset your password"/>
<!doctype html>
<html lang="en">
<head>
  <%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body class="auth">
  <%@ include file="/WEB-INF/jspf/brand.jspf" %>

  <main class="auth-main">
    <div class="auth-card">
      <h1>Reset your password</h1>
      <p class="intro">Confirm the details you registered with, then choose a new password.</p>

      <c:if test="${not empty error}">
        <p class="notice error" role="alert">${fn:escapeXml(error)}</p>
      </c:if>

      <form method="post" action="${ctx}/forgot-password" novalidate>
        <ui:field name="userId" label="User ID" value="${param.userId}" autocomplete="username"
                  error="${errors.userId}" autofocus="${empty errors and empty error}"/>
        <ui:field name="email" type="email" label="Email" value="${param.email}" autocomplete="email"
                  error="${errors.email}"/>
        <ui:field name="dateOfBirth" type="date" label="Date of birth" value="${param.dateOfBirth}" autocomplete="bday"
                  error="${errors.dateOfBirth}"/>
        <ui:field name="password" type="password" label="New password" autocomplete="new-password"
                  hint="At least 8 characters, with a letter and a number." error="${errors.password}"/>
        <ui:field name="confirmPassword" type="password" label="Confirm new password" autocomplete="new-password"/>

        <button class="btn-primary" type="submit">Reset password</button>
      </form>

      <p class="switch"><a href="${ctx}/login">Back to log in</a></p>
    </div>
  </main>
</body>
</html>
