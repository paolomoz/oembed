<%--

  OEmbed Embedder component.

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page import="com.adobe.cq.components.oembed.OEmbedRenderer,
  com.adobe.cq.components.oembed.OEmbedType,
  com.day.cq.wcm.api.WCMMode" %><%
    
    boolean isEdit = WCMMode.fromRequest(request) == WCMMode.EDIT;
    String url = properties.get("webpage", String.class);
    if ((url==null||url.length()==0)&&isEdit) {
      %><strong class="error">Please configure the URL to embed</strong><%
    } else {
      OEmbedRenderer renderer = (OEmbedRenderer) sling.getService(com.adobe.cq.components.oembed.OEmbedLookup.class);
      boolean found = renderer.discoverLink(url);
      if ((!found||renderer.getType()==null)&&isEdit) {
        %><strong class="error">URL <%=url %> cannot be embedded.</strong><%
      } else if (found&&renderer.getType()!=null) {
        if (renderer.getType()==OEmbedType.RICH||renderer.getType()==OEmbedType.VIDEO) {
          %>
            <div class="oembed-rich">
              <%= renderer.getHTML() %>
            </div>
          <%
        } else if (renderer.getType()==OEmbedType.PHOTO) {
          int width = renderer.getWidth();
          int height = renderer.getHeight();
          String photoUrl = renderer.getURL();
          String title = renderer.getTitle();
          %><img src="<%= photoUrl %>" width="<%= width %>" height="<%= height %>" title="<%= title %>"/><%
        } else if (renderer.getType()==OEmbedType.CARD) {
          %>
            <div class="oembed-card">
              <a href="<%=webpage %>" class="oembed-card-link oembed-card-image-link"><img class="oembed-card-image" src="<%=renderer.getThumbnailURL() %>"></a>
              <h2 class="oembed-card-title"><a href="<%=webpage %>" class="oembed-card-link oembed-card-title-link"><%=renderer.getTitle() %></a></h2>
              <p class="oembed-card-description"><a href="<%=webpage %>" class="oembed-card-link oembed-card-description-link"><%=renderer.getDescription() %></a></p>
            </div>
          <%
        } else {
          %>trying to embed this stuff<%
        }
      }
    }

%>

<%--
<%
      
   	String endpoint = properties.get("endpoint", String.class);
	
   	String webpage = properties.get("webpage", String.class);
	OEmbedRenderer renderer = new OEmbedRenderer();
	boolean found = false;
	if (endpoint != null && url != null) {
		Integer maxWidth = properties.get("maxWidth", Integer.class);
		Integer maxHeight = properties.get("maxHeight", Integer.class);
		found = renderer.fetchResponse(endpoint, url, maxWidth, maxHeight);
    } else if (webpage != null) {
		found = renderer.discoverLink(webpage);
    }
	if (found) {
%><div style="margin-bottom:10px;"><%
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
    } else {
%><p style="border">Please configure OEmbed URL</p><%
    }
%></div>
--%>