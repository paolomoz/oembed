<%@taglib prefix="slingjsp" uri="http://sling.apache.org/taglibs/sling" %>
<%@include file="/libs/foundation/global.jsp"%>
<c:forEach var="child" items="${slingjsp:listChildren(resource)}">
  <cq:include path="${child.path}" defaultResourceType="oembed/components/embedder"/>
</c:forEach>