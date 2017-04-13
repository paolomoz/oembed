<%@taglib prefix="slingjsp" uri="http://sling.apache.org/taglibs/sling" %>
<%@include file="/libs/foundation/global.jsp"%>
<c:set var="max" value="${slingjsp:getValue(properties,'items',3)}" />
<c:forEach var="child" items="${slingjsp:listChildren(resource)}" end="${max - 1}">
  <cq:include path="${child.path}" defaultResourceType="oembed/components/embedder"/>
</c:forEach>