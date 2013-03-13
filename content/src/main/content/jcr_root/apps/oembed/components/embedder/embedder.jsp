<%--

  OEmbed Embedder component.

  

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page import="com.adobe.aem.wcm.components.oembed.OEmbedRenderer,
    com.adobe.aem.wcm.components.oembed.OEmbedType" %><%
   	String endpoint = properties.get("endpoint", String.class);
	String url = properties.get("url", String.class);
   	String webpage = properties.get("webpage", String.class);
	OEmbedRenderer renderer = new OEmbedRenderer();
	boolean found = false;
	if (endpoint != null && url != null) {
		Integer maxWidth = properties.get("maxWidth", Integer.class);
		Integer maxHeight = properties.get("maxHeight", Integer.class);
		found = renderer.fetchResponse(endpoint, url, maxWidth, maxHeight);
    } else if (webpage != null) {
		found = renderer.discoverLink(webpage);
    } else {
%><p>OEmbed - Double-click to configure.</p><%
    }
	if (found) {
		String title = renderer.getTitle();
		if (title == null) { title = "No title"; }
		switch (renderer.getType()) {
	    	case PHOTO:
			int width = renderer.getWidth();
			int height = renderer.getHeight();
			String photoUrl = renderer.getURL();
%><img src="<%= photoUrl %>" width="<%= width %>" height="<%= height %>" title="<%= title %>"/><%
	        break;
	    	case VIDEO:
	    	case RICH:
%><%= renderer.getHTML() %><%
	        break;
	    	case LINK:
%><a href="<%= renderer.getURL() %>"><%= renderer.getTitle() %></a><%
	        break;
	    }
    }
%>