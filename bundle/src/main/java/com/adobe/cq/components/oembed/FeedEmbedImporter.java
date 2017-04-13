package com.adobe.cq.components.oembed;

import com.day.cq.polling.importer.HCImporter;
import com.day.cq.polling.importer.Importer;
import java.io.InputStream;
import org.apache.sling.api.resource.Resource;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndEntry;
import java.io.InputStreamReader;
import org.apache.felix.scr.annotations.*;
import java.io.IOException;
import com.rometools.rome.io.FeedException;
import org.apache.sling.commons.osgi.PropertiesUtil;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Component(metatype = false, enabled = true, label = "OEmbed feed importer")
@Service(value = Importer.class)
@Property(name=Importer.SCHEME_PROPERTY, value="embed", propertyPrivate = true)
public class FeedEmbedImporter extends HCImporter implements Importer {   
  private static final String DEFAULT_SCHEME = "embed";
  
  
  private static final String PROP_SCHEME = Importer.SCHEME_PROPERTY;
  
  private String scheme;
  
  @Activate
  protected void activate(Map<String, Object> properties) {
    this.scheme = PropertiesUtil.toString(properties.get(PROP_SCHEME), DEFAULT_SCHEME);
  }
 
  
  public void importData(String scheme, InputStream data,
    String characterEncoding, Resource target) throws IOException {
      System.out.println("Getting feed with character encoding: " + characterEncoding);
      try {
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new InputStreamReader(data, characterEncoding));
        Node node = target.adaptTo(Node.class);
        NodeIterator children = node.getNodes();
        while (children.hasNext()) {
          Node child = children.nextNode();
          try {
            if (child.getProperty("webpage").getString().equals(feed.getEntries().get(0).getLink())) {
              return;
            }
          } catch (RepositoryException re) {
            //node does not exist, remove all children
          }
          child.remove();
        }
        int i = 1;
        for (SyndEntry entry : feed.getEntries()) {
          String link = entry.getLink();
          Node child = node.addNode("embed" + i++);
          child.setProperty("sling:resourceType","oembed/components/embedder");
          child.setProperty("webpage", link);
        }
        node.getSession().save();
      } catch (FeedException fe) {
        throw new IOException(fe);
      } catch (RepositoryException re) {
        throw new IOException(re);
      }
    }
    
  public List<String> getFeedTitles(InputStream data,
  String characterEncoding) {
    return new ArrayList<String>();
  }
    
}