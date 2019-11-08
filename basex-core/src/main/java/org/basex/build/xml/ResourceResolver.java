package org.basex.build.xml;

import java.io.*;

import org.w3c.dom.ls.*;

/**
 * Resource resolver.
 *
 * @author BaseX Team 2005-19, BSD License
 * @author Christian Gruen
 */
public class ResourceResolver implements LSResourceResolver {
  @Override
  public LSInput resolveResource(final String type, final String namespaceURI,
      final String publicId, final String systemId, final String baseURI) {
    final String sid = systemId.contains("standards.iso.org") ?
      systemId.replace("http:", "https:") : systemId;
    return new CatalogInput(publicId, sid, baseURI);
  }

  /**
   * Catalog input.
   *
   * @author BaseX Team 2005-19, BSD License
   * @author Christian Gruen
   */
  private static class CatalogInput implements LSInput {
    /** Character stream. */
    private Reader characterStream;
    /** Byte stream. */
    private InputStream byteStream;
    /** String data. */
    private String stringData;
    /** System ID. */
    private String systemId;
    /** Public ID. */
    private String publicId;
    /** Base URI. */
    private String baseURI;
    /** Encoding. */
    private String encoding;
    /** Certified text. */
    private boolean certifiedText;

    /**
     * Constructor.
     * @param publicId public ID
     * @param systemId system ID
     * @param baseURI base URI
     */
    public CatalogInput(final String publicId, final String systemId, final String baseURI) {
      this.publicId = publicId;
      this.systemId = systemId;
      this.baseURI = baseURI;
    }

    @Override
    public Reader getCharacterStream() {
      return characterStream;
    }

    @Override
    public void setCharacterStream(final Reader characterStream) {
      this.characterStream = characterStream;
    }

    @Override
    public InputStream getByteStream() {
      return byteStream;
    }

    @Override
    public void setByteStream(final InputStream byteStream) {
      this.byteStream = byteStream;
    }

    @Override
    public String getStringData() {
      return stringData;
    }

    @Override
    public void setStringData(final String stringData) {
      this.stringData = stringData;
    }

    @Override
    public String getSystemId() {
      return systemId;
    }

    @Override
    public void setSystemId(final String systemId) {
      this.systemId = systemId;
    }

    @Override
    public String getPublicId() {
      return publicId;
    }

    @Override
    public void setPublicId(final String publicId) {
      this.publicId = publicId;
    }

    @Override
    public String getBaseURI() {
      return baseURI;
    }

    @Override
    public void setBaseURI(final String baseURI) {
      this.baseURI = baseURI;
    }

    @Override
    public String getEncoding() {
      return encoding;
    }

    @Override
    public void setEncoding(final String encoding) {
      this.encoding = encoding;
    }

    @Override
    public boolean getCertifiedText() {
      return certifiedText;
    }

    @Override
    public void setCertifiedText(final boolean certifiedText) {
      this.certifiedText = certifiedText;
    }
  }
}
