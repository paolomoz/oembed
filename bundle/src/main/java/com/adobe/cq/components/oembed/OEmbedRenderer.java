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
  
  public LinkDiscoverer getLinkDiscoverer() {
    return new LinkDiscoverer(this.endpoints);
  }
}
