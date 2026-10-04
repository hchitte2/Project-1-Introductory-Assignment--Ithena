<c:set var="pageTitle" value="Home"/>
<!doctype html>
<html lang="en">
<head>
  <%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body class="app">
  <header class="topbar">
    <a class="wordmark" href="${ctx}/home">Member Portal</a>
    <form method="post" action="${ctx}/logout">
      <button class="btn-quiet" type="submit">Log out</button>
    </form>
  </header>

  <main class="home">
    <section class="welcome">
      <img class="avatar" src="${ctx}/photo" alt="Profile photo of ${fn:escapeXml(user.fullName)}" width="152" height="152">
      <div>
        <p class="today">Today is <time datetime="${todayIso}">${today}</time></p>
        <h1>Welcome, ${fn:escapeXml(user.fullName)}</h1>
      </div>
    </section>

    <section class="account" aria-labelledby="account-heading">
      <h2 id="account-heading">Your account</h2>
      <dl class="details">
        <div><dt>User ID</dt><dd>${fn:escapeXml(user.userId)}</dd></div>
        <div><dt>Email</dt><dd>${fn:escapeXml(user.email)}</dd></div>
        <div><dt>Date of birth</dt><dd>${dateOfBirth}</dd></div>
        <div><dt>Member since</dt><dd>${memberSince}</dd></div>
      </dl>
    </section>
  </main>
</body>
</html>
