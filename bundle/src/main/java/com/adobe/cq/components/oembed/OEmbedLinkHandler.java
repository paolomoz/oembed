package com.adobe.cq.components.oembed;

import static org.apache.tika.sax.XHTMLContentHandler.XHTML;

import java.util.ArrayList;
import java.util.List;

import org.apache.tika.sax.Link;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


public class OEmbedLinkHandler extends DefaultHandler {

	public static final String LINK_TYPE_OEMBED_JSON = "application/json+oembed";
	
	/** Collected links */
	private final List<Link> links = new ArrayList<Link>();
  
  private boolean card = false;
  private String title;
  private String description;
  private String image;
  private String canonical;
  
  private boolean recordTitle = false;
  private StringBuffer titleRecorder;

    /**
     * Returns the list of collected links.
     *
     * @return collected links
     */
    public List<Link> getLinks() {
        return links;
    }
    
    public boolean hasCard() {
      return this.card;
    }
    
    public boolean hasTitle() {
      return this.title != null;
    }
    
    public String getTitle() {
      return this.title;
    }
    
    public String getCanonicalURL() {
      return this.canonical;
    }
    
    public String getImageURL() {
      return this.image;
    }
    
    public String getDescription() {
      return this.description;
    }

	@Override
	public void startElement(String uri, String local, String name, Attributes attributes) {
		if (XHTML.equals(uri)) {
			if ("link".equals(local)) {
				String type = attributes.getValue("", "type");
				if (LINK_TYPE_OEMBED_JSON.equals(type)) {
					String href = attributes.getValue("", "href");
					String title = attributes.getValue("", "title");
					String rel = attributes.getValue("", "rel");
					Link link = new Link(type, href, title, "", rel);
					links.add(link);
        } else if ("canonical".equals(attributes.getValue("", "rel"))) {
          this.canonical = attributes.getValue("","href");
				}
			} else if ("meta".equals(local)) {
        String metaName = attributes.getValue("", "name");
        String metaContent = attributes.getValue("", "content");
        //System.out.println("meta name="+metaName+" content="+metaContent);
        if ("twitter:title".equals(metaName)||"og:title".equals(metaName)) {
          //System.out.println("HEY HEY HEY I HAVE FOUND A CARD HEY HEY HEY");
          this.title = metaContent;
          this.card = true;
        } else if ("twitter:description".equals(metaName)||"og:description".equals(metaName)) {
          this.description = metaContent;
          this.card = true;
        } else if ("twitter:image".equals(metaName)||"og:image".equals(metaName)) {
          this.image = metaContent;
          this.card = true;
        } 
			} else if ("title".equals(local)&&this.title==null) {
        this.recordTitle = true;
        this.titleRecorder = new StringBuffer();
			}
		}
	}
  
  public void endElement(String uri, String localName, String qName) {
    if(XHTML.equals(uri)&&"title".equals(localName)&&recordTitle) {
      recordTitle = false;
      this.title = this.titleRecorder.toString();
      this.titleRecorder = null;
    }
  }
  
  public void characters(char[] ch, int start, int length) {
    if (this.titleRecorder!=null&&this.recordTitle) {
      this.titleRecorder.append(new String(ch, start, length));
    }
  }
}
