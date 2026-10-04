<c:set var="pageTitle" value="Create an account"/>
<!doctype html>
<html lang="en">
<head>
  <%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body class="auth">
  <%@ include file="/WEB-INF/jspf/brand.jspf" %>

  <main class="auth-main">
    <div class="auth-card wide">
      <h1>Create an account</h1>
      <p class="intro">Every field is required.</p>

      <c:if test="${not empty errors}">
        <p class="notice error" role="alert">Some details need fixing. See the highlighted fields below.</p>
      </c:if>

      <form method="post" action="${ctx}/register" enctype="multipart/form-data" novalidate>
        <fieldset>
          <legend>About you</legend>
          <div class="row">
            <ui:field name="firstName" label="First name" value="${param.firstName}" autocomplete="given-name"
                      error="${errors.firstName}" autofocus="${empty errors}"/>
            <ui:field name="lastName" label="Last name" value="${param.lastName}" autocomplete="family-name"
                      error="${errors.lastName}"/>
          </div>
          <ui:field name="email" type="email" label="Email" value="${param.email}" autocomplete="email"
                    error="${errors.email}"/>
          <ui:field name="dateOfBirth" type="date" label="Date of birth" value="${param.dateOfBirth}" autocomplete="bday"
                    error="${errors.dateOfBirth}"/>

          <div class="field${not empty errors.photo ? ' has-error' : ''}">
            <label for="photo">Profile photo</label>
            <div class="photo-picker">
              <div class="photo-frame">
                <img id="photo-preview" alt="Preview of your selected photo" hidden>
              </div>
              <div>
                <input id="photo" name="photo" type="file" accept="image/jpeg,image/png,image/gif,image/webp" required
                       aria-describedby="photo-note"${not empty errors.photo ? ' aria-invalid="true"' : ''}>
                <c:choose>
                  <c:when test="${not empty errors.photo}">
                    <p class="note error-text" id="photo-note">${fn:escapeXml(errors.photo)}</p>
                  </c:when>
                  <c:when test="${not empty errors}">
                    <p class="note" id="photo-note">Choose your photo again. Files aren't kept when a form has errors.</p>
                  </c:when>
                  <c:otherwise>
                    <p class="note" id="photo-note">JPG, PNG, GIF, or WebP, up to 2 MB.</p>
                  </c:otherwise>
                </c:choose>
              </div>
            </div>
          </div>
        </fieldset>

        <fieldset>
          <legend>Sign-in details</legend>
          <ui:field name="userId" label="User ID" value="${param.userId}" autocomplete="username"
                    hint="3–20 characters: letters, numbers, dots, hyphens, or underscores." error="${errors.userId}"/>
          <div class="row">
            <ui:field name="password" type="password" label="Password" autocomplete="new-password"
                      hint="At least 8 characters, with a letter and a number." error="${errors.password}"/>
            <ui:field name="confirmPassword" type="password" label="Confirm password" autocomplete="new-password"/>
          </div>
        </fieldset>

        <button class="btn-primary" type="submit">Create account</button>
      </form>

      <p class="switch">Already have an account? <a href="${ctx}/login">Log in</a></p>
    </div>
  </main>

  <script src="${ctx}/assets/js/photo-preview.js" defer></script>
</body>
</html>
