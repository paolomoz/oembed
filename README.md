# AEM Ober-Embed Component

Do you love having AEM sites with rich content, but hate creating rich content? *Why write content when you can just stea^H embed it?*

Ober-Embed is a component for [Adobe Experience Manager](http://www.adobe.com/solutions/web-experience-management.html) that allows you to embed content from other websites simply by providing a URL. Ober-Embed tries to find the best way to embed the content, using:

* [oEmbed](http://oembed.com/) Autodiscovery (supported by YouTube, Twitter, and more)
* [oEmbed](http://oembed.com/) manual discovery using configured OEmbed providers (I recommend [Noembed](https://noembed.com) for Amazon, Gyfcat, XKCD)
* [Twitter Cards](https://dev.twitter.com/cards/markup) for many new websites
* [Facebook OpenGraph markup](https://developers.facebook.com/docs/sharing/webmasters) for most other websites
* [Plain old HTML <title> tags](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/title) for literally any other website

The component can embed content, including title, description, images, videos or even interactive content (using OEmbed). Because the OEmbed providers are configurable, you can set your own providers (for instance using [Adobe I/O Runtime](https://www.adobe.io/apis/cloudplatform/runtime.html)) or switch providers if you need more sites supported. The OEmbed providers are configured in the OSGi console, so that authors only have to know the URL of the page they want to embed.
  
## Installation

To install the component:

* Build and install the component into your AEM instance using [Apache Maven](http://maven.apache.org/):
```
mvn -P installPackage install
```
* Enable the component for your page in [design view](http://dev.day.com/docs/en/cq/current/wcm/working_with_cq_wcm/using_edit_designandpreviewmodes.html#Design Mode).

## Todo
- [ ] white list of sites in design configuration
- [ ] screen shots of embedded sites

## Thanks
* [Paolo](https://github.com/paolomoz/oembed)
* [Ugo](https://github.com/ugocei/oembed)

## FAQ
### Why is it called Ober-Embed?

Because it's based on OEmbed and Uber-Embed would be too cliche.