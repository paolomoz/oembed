package com.adobe.cq.components.oembed;

import com.day.cq.polling.importer.HCImporter;
import com.day.cq.polling.importer.Importer;
import java.io.InputStream;
import org.apache.sling.api.resource.Resource;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.feed.synd.SyndFeed;
import java.io.InputStreamReader;
import org.apache.felix.scr.annotations.*;
import java.io.IOException;
import com.rometools.rome.io.FeedException;

@Component(metatype = false, enabled = true, label = "OEmbed feed importer")
@Service
@Property(name = Importer.SCHEME_PROPERTY, value = "embed")
public class FeedEmbedImporter extends HCImporter implements Importer {    
  public void importData(String scheme, InputStream data,
    String characterEncoding, Resource target) throws IOException {
      try {
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new InputStreamReader(data, characterEncoding));
      } catch (FeedException fe) {
        throw new IOException(fe);
      }
      
    }
}