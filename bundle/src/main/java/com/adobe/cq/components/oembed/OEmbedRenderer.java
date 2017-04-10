package com.adobe.cq.components.oembed;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.tika.exception.TikaException;
import org.apache.tika.sax.Link;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;

@Component(immediate = true,metatype=true)
@Service(OEmbedLookup.class)
public class OEmbedRenderer implements OEmbedLookup {


	private JSONObject data;
  private OEmbedLinkHandler card;
	private HttpClient client = new HttpClient();
	private LinkFinder linkFinder = new LinkFinder();
	private final Logger logger = LoggerFactory. getLogger(OEmbedRenderer.class);
  
  @Property(value={"http://noembed.com/embed"}, unbounded = PropertyUnbounded.ARRAY, label = "OEmbed Lookup endpoints", cardinality = 5, description = "Enter additional URLs to look up OEmbed configurations.")
  private static final String ENDPOINTS = "endpoints";
  private List<String> endpoints;
  
  
  @Activate
  protected void activate(@SuppressWarnings("rawtypes") final Map context) {
    System.out.println("activate");
    this.endpoints = Arrays.asList(PropertiesUtil.toStringArray(context.get(ENDPOINTS)));
    System.out.println(this.endpoints.size());
  }
  
  @Modified
  protected void modified(ComponentContext context) {
    System.out.println("modified");
    this.endpoints = Arrays.asList(PropertiesUtil.toStringArray(context.getProperties().get(ENDPOINTS)));
    System.out.println(this.endpoints.size());
  }
  
  public List<String> getOEmbedEndpoints() {
    return this.endpoints;
  }
  
  public void setOEmbedEndpoints(List<String> endpoints) {
    this.endpoints = endpoints;
  }
  
  /**
   * Attempts to find a OEmbed link through auto-discovery, then falls back to
   * the configured endpoints.
   */
	public boolean discoverLink(String url) {
    System.out.println("Getting URL: " + url);
		HttpMethod method = null;
        try {
			method = new GetMethod(url);
	    	method.setFollowRedirects(true);
	    	client.executeMethod(method);
	    	InputStream input = method.getResponseBodyAsStream();
        //first of all, parse the document
	    	List<Link> links = linkFinder.findLinks(input);
        //if no OEmbed links have been found, try the configured OEmbed providers, one by one
	    	if (links.isEmpty()) {
          if (this.endpoints!=null) {
            System.out.println("Finding endpoints. " + this.endpoints.size() + " endpoints configured");
            for (String endpoint : this.endpoints) {
              System.out.println("Trying " + endpoint);
              boolean found = fetchResponse(endpoint, url, null, null);
              
              if (found) {
                System.out.println(endpoint + " found");
                return true;
              }
            }
          } else {
            System.out.println("No OEmbed endpoints configured");
          }
          //if no OEmbed representation can be found, try Twitter cards or Open Graph meta tags
          if (this.linkFinder.getLinkHandler().hasCard()) {
            this.card = this.linkFinder.getLinkHandler();
            System.out.println("Hey, I've found a Twitter card!");
            return true;
          }
          return false;
	    	} else {
          //an OEmbed link has been found, so we will use this one.
	    	  return fetchResponse(links.get(0).getUri());
        }
		} catch (IOException e) {
			throw new OEmbedException(e);
		} catch (SAXException e) {
			throw new OEmbedException(e);
		} catch (TikaException e) {
			throw new OEmbedException(e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		
	}
	
	public boolean fetchResponse(String endpoint, String url, Integer maxWidth, Integer maxHeight) {
        	try {
				url = URLEncoder.encode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new OEmbedException(e);
			}
        	String requestUrl = endpoint + "?url=" + url +
        			"&format=json";
        	if (maxWidth != null) {
        		requestUrl += "&maxwidth=" + maxWidth;
        	}
        	if (maxHeight != null) {
        		requestUrl += "&maxheight=" + maxHeight;
        	}
        	return fetchResponse(requestUrl);
	}
	
	public boolean fetchResponse(String requestUrl) {
		logger.debug("Requesting URL " + requestUrl);
		HttpMethod method = null;
        try {
			method = new GetMethod(requestUrl);
	    	method.setFollowRedirects(true);
	        // Create a response handler
	    	client.executeMethod(method);
	    	String responseBody = method.getResponseBodyAsString();
	    	data = new JSONObject(responseBody);
        if (this.getType()!=null) {
          return true;
        } else {
          return false;
        }
		} catch (IOException e) {
			throw new OEmbedException(e);
		} catch (JSONException e) {
			throw new OEmbedException("Input is not well-formed JSON", e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}

	public OEmbedType getType() {
		try {
      if (this.card!=null) {
        return OEmbedType.CARD;
      }
			return OEmbedType.valueOf(data.getString("type").toUpperCase());
		} catch (JSONException e) {
      logger.warn("No 'type' attribute in JSON input " + data.toString());
      return null;
		}
	}
	
	public String getTitle() {
    if (this.card!=null) {
      return this.card.getTitle();
    }
		try {
			return data.has("title") ? data.getString("title") : null; 
		} catch (JSONException e) {
			throw new OEmbedException(e);
		}
	}
	
	public String getURL() {
    if (this.card!=null) {
      return this.card.getCanonicalURL();
    }
		try {
			return data.has("url") ? data.getString("url") : null; 
		} catch (JSONException e) {
			throw new OEmbedException(e);
		}
	}
	
	public String getHTML() {
    if (this.card!=null) {
      return this.card.getDescription();
    }
		try {
			return data.has("html") ? data.getString("html") : null; 
		} catch (JSONException e) {
			throw new OEmbedException(e);
		}
	}
	
	public int getWidth() {
		try {
			return data.getInt("width");
		} catch (JSONException e) {
			throw new OEmbedException("No 'width' attribute in JSON input", e);
		}
	}
	
	public int getHeight() {
		try {
			return data.getInt("height");
		} catch (JSONException e) {
			throw new OEmbedException("No 'height' attribute in JSON input", e);
		}
	}
	
	public String getAuthorName() {
		try {
			return data.has("author_name") ? data.getString("author_name") : null; 
		} catch (JSONException e) {
			throw new OEmbedException(e);
		}
	}
	
	public String getAuthorURL() {
		try {
			return data.has("author_url") ? data.getString("author_url") : null; 
		} catch (JSONException e) {
			throw new OEmbedException(e);
		}
	}
	
	public String getProviderName() {
		try {
			return data.has("provider_name") ? data.getString("provider_name") : null; 
		} catch (JSONException e) {
			throw new OEmbedException(e);
		}
	}
	
	public String getProviderURL() {
		try {
			return data.has("provider_url") ? data.getString("provider_url") : null; 
		} catch (JSONException e) {
			throw new OEmbedException(e);
		}
	}
	
	public String getThumbnailURL() {
    if (this.card!=null) {
      return this.card.getImageURL();
    }
		try {
			return data.has("thumbnail_url") ? data.getString("thumbnail_url") : null; 
		} catch (JSONException e) {
			throw new OEmbedException(e);
		}
	}
	
	public int getThumbnailWidth() {
		try {
			return data.getInt("thumbnail_width");
		} catch (JSONException e) {
			throw new OEmbedException("No 'thumbnail_width' attribute in JSON input", e);
		}
	}
	
	public int getThumbnailHeight() {
		try {
			return data.getInt("thumbnail_height");
		} catch (JSONException e) {
			throw new OEmbedException("No 'thumbnail_height' attribute in JSON input", e);
		}
	}
	
}
