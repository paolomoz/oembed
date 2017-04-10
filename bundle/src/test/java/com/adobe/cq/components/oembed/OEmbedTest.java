package com.adobe.cq.components.oembed;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.List;
import java.util.Arrays;

import org.apache.tika.sax.Link;
import org.junit.Test;

import com.adobe.cq.components.oembed.LinkFinder;
import com.adobe.cq.components.oembed.OEmbedRenderer;
import com.adobe.cq.components.oembed.LinkDiscoverer;
import com.adobe.cq.components.oembed.OEmbedType;

public class OEmbedTest {

	@Test
	public void testPhoto() {
		String endpoint = "http://www.flickr.com/services/oembed";
		String url = "http://flickr.com/photos/bees/2362225867";
		LinkDiscoverer renderer = new OEmbedRenderer().getLinkDiscoverer();
		renderer.fetchResponse(endpoint, url, 240, 180);
		assertEquals(OEmbedType.PHOTO, renderer.getType());
		assertEquals("https://farm4.staticflickr.com/3040/2362225867_4a87ab8baf_m.jpg", renderer.getURL());
		assertEquals(240, renderer.getWidth());
		assertEquals(180, renderer.getHeight());
	}

	@Test
	public void testDiscoverVideo() {
		String url = "https://www.youtube.com/watch?v=9bZkp7q19f0";
		LinkDiscoverer renderer = new OEmbedRenderer().getLinkDiscoverer();
    assertTrue(renderer.discoverLink(url));
		//renderer.fetchResponse(endpoint, url, null, null);
		assertEquals(OEmbedType.VIDEO, renderer.getType());
		System.out.println(renderer.getHTML());
	}
  
  @Test
  public void testDefaultFallback() {
    String url = "https://gfycat.com/WindyHiddenGemsbok";
    LinkDiscoverer renderer = new OEmbedRenderer().getLinkDiscoverer();
    assertTrue(renderer.discoverLink(url));
    assertEquals(OEmbedType.VIDEO, renderer.getType());
    System.out.println(renderer.getHTML());
  }
  
  @Test
  public void testFallback() {
    String url = "https://github.com/trieloff/github-oembed/blob/master/oembed.js";
    LinkDiscoverer renderer = new OEmbedRenderer().getLinkDiscoverer();
    assertTrue(renderer.fetchResponse("https://runtime-preview.adobe.io/github.com/trieloff/github-oembed/master/github-oembed/embed", url, null, null));
    assertEquals(OEmbedType.RICH, renderer.getType());
    System.out.println(renderer.getHTML());
  }
  
  @Test
  public void testDefaultDiscovery() {
    String url = "https://gist.github.com/trieloff/013509c40db9860746fe3977acadb676";
    OEmbedRenderer r = new OEmbedRenderer();
    r.setOEmbedEndpoints(Arrays.asList(new String[] {"https://runtime-preview.adobe.io/github.com/trieloff/github-oembed/master/github-oembed/embed"}));
    
    LinkDiscoverer renderer = r.getLinkDiscoverer();
    
    assertTrue(renderer.discoverLink(url));
    assertEquals(OEmbedType.RICH, renderer.getType());
  }
  
  @Test
  public void testDefaultDiscoveryGist() {
    String url = "https://github.com/trieloff/github-oembed/blob/master/oembed.js";
    OEmbedRenderer r = new OEmbedRenderer();
    r.setOEmbedEndpoints(Arrays.asList(new String[] {"https://runtime-preview.adobe.io/github.com/trieloff/github-oembed/master/github-oembed/embed"}));
    
    LinkDiscoverer renderer = r.getLinkDiscoverer();
    
    assertTrue(renderer.discoverLink(url));
    assertEquals(OEmbedType.RICH, renderer.getType());
  }
	
	@Test
	public void testReadLinks() throws Exception {
		LinkFinder finder = new LinkFinder();
		InputStream input = this.getClass().getResourceAsStream("/flickr.html");
		List<Link> links = finder.findLinks(input);
		assertEquals(1, links.size());
		Link link = links.get(0);
		assertEquals("http://www.flickr.com/services/oembed?url=http%3A%2F%2Fwww.flickr.com%2Fphotos%2Fugocei%2F8473777164%2F&format=json",
				link.getUri());
		
	}
  
  
  
  @Test
  public void testTwitterCardsMedium() throws Exception {
    String url = "https://medium.com/adobe-io/what-event-based-architectures-mean-for-application-integration-662932169d28";
    LinkDiscoverer renderer = new OEmbedRenderer().getLinkDiscoverer();
    assertTrue(renderer.discoverLink(url));
    assertEquals(OEmbedType.CARD, renderer.getType());
  }
  
  
  @Test
  public void testTwitterCards() throws Exception {
    String url = "http://www.spiegel.de/wissenschaft/natur/great-barrier-reef-zwei-drittel-der-korallen-drohen-abzusterben-a-1142645.html";
    LinkDiscoverer renderer = new OEmbedRenderer().getLinkDiscoverer();
    assertTrue(renderer.discoverLink(url));
    assertEquals(OEmbedType.CARD, renderer.getType());
  }

	@Test
	public void testDiscoverPhoto() {
		String url = "http://flickr.com/photos/bees/2362225867";
		LinkDiscoverer renderer = new OEmbedRenderer().getLinkDiscoverer();
		assertTrue(renderer.discoverLink(url));
		assertEquals(OEmbedType.PHOTO, renderer.getType());
		assertEquals("https://farm4.staticflickr.com/3040/2362225867_4a87ab8baf_b.jpg", renderer.getURL());
		assertEquals(1024, renderer.getWidth());
		assertEquals(768, renderer.getHeight());
	}

}
