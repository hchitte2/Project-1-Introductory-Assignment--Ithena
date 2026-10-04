<%@ tag body-content="empty" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%--
  A labelled form input with an optional hint and an inline error message.
  Optional attributes are read via pageScope: an attribute that is not passed is not defined,
  and a bare ${error} would fall through to a request attribute with the same name.
--%>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="type" %>
<%@ attribute name="value" %>
<%@ attribute name="autocomplete" %>
<%@ attribute name="hint" %>
<%@ attribute name="error" %>
<%@ attribute name="autofocus" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="err" value="${pageScope.error}"/>
<c:set var="tip" value="${pageScope.hint}"/>
<div class="field${not empty err ? ' has-error' : ''}">
  <label for="${name}">${fn:escapeXml(label)}</label>
  <input id="${name}" name="${name}" type="${empty pageScope.type ? 'text' : pageScope.type}" value="${fn:escapeXml(pageScope.value)}" required
    <c:if test="${not empty pageScope.autocomplete}"> autocomplete="${pageScope.autocomplete}"</c:if>
    <c:if test="${pageScope.autofocus}"> autofocus</c:if>
    <c:if test="${not empty tip or not empty err}"> aria-describedby="${name}-note"</c:if>
    <c:if test="${not empty err}"> aria-invalid="true"</c:if>>
  <c:choose>
    <c:when test="${not empty err}"><p class="note error-text" id="${name}-note">${fn:escapeXml(err)}</p></c:when>
    <c:when test="${not empty tip}"><p class="note" id="${name}-note">${fn:escapeXml(tip)}</p></c:when>
  </c:choose>
</div>
