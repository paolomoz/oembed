<%@taglib prefix="slingjsp" uri="http://sling.apache.org/taglibs/sling" %>
<%@include file="/libs/foundation/global.jsp"%>
<%
  int items = properties.get("items", 3);
  int designItems = currentStyle.get("items",1000);
  int end = Math.min(items, designItems);
  end--;
  if (end<0) {
    end = 0;
  }
  pageContext.setAttribute("end", end);
%>
<c:set var="max" value="${end}" />
<c:forEach var="child" items="${slingjsp:listChildren(resource)}" end="${end}">
  <cq:include path="${child.path}" defaultResourceType="oembed/components/embedder"/>
</c:forEach>