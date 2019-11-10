package org.basex.build.xml;

import static org.basex.util.Reflect.*;

import java.lang.reflect.*;

import javax.xml.transform.*;

import org.basex.core.*;
import org.basex.util.*;
import org.w3c.dom.ls.*;
import org.xml.sax.*;

/**
 * Wraps the CatalogResolver object.
 * Searches for presence of one of the XML resolver packages
 * {@code org.apache.xml.resolver.tools.CatalogResolver} or
 * {@code code com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver}.
 *
 * The catalog manager is part of Java 9; in future, we can possibly drop this class.
 *
 * @author BaseX Team 2005-19, BSD License
 * @author Michael Seiferle
 * @author Liam Quin
 */
public final class CatalogWrapper {
  /** CatalogManager class. */
  private static final Class<?> MANAGER;
  /** CatalogResolver constructor. */
  private static final Constructor<?> RESOLVER;
  /** CatalogResolver constructor. */
  private static final Constructor<?> RESOURCERESOLVER;

  static {
    // try to locate catalog manager from xml-resolver-1.2.jar library
    Class<?> manager = find("org.apache.xml.resolver.CatalogManager"), resolver;
    if(manager != null) {
      resolver = find("org.apache.xml.resolver.tools.CatalogResolver");
    } else {
      // try to resort to internal catalog manager
      manager = find("com.sun.org.apache.xml.internal.resolver.CatalogManager");
      resolver = find("com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver");
    }
    MANAGER = manager;
    RESOLVER = find(resolver, manager);

    Class<?> rresolver = find("org.apache.xerces.util.XMLCatalogResolver");
    if(rresolver == null) {
      rresolver = find("com.sun.org.apache.xerces.internal.util.XMLCatalogResolver");
    }
    RESOURCERESOLVER = find(rresolver, String[].class);
  }

  /** Instance of catalog manager. */
  private final Object cm = Reflect.get(MANAGER);
  /** Catalog files. */
  private final String catfile;

  /**
   * Checks if the catalog manager is available.
   * @return result of check
   */
  public static boolean available() {
    return MANAGER != null;
  }

  /**
   * Constructor.
   * @param ctx database context
   */
  public CatalogWrapper(final Context ctx) {
    this(ctx.options.get(MainOptions.CATFILE));
  }

  /**
   * Constructor.
   * @param catfile semicolon-separated list of catalog files
   */
  public CatalogWrapper(final String catfile) {
    this.catfile = catfile;
    if(!catfile.isEmpty() && available()) {
      if(System.getProperty("xml.catalog.ignoreMissing") == null) {
        invoke(method(MANAGER, "setIgnoreMissingProperties", boolean.class), cm, true);
      }
      invoke(method(MANAGER, "setCatalogFiles", String.class), cm, catfile);
    }
  }

  /**
   * Returns a URI resolver.
   * @return URI resolver (can be {@code null})
   */
  public URIResolver getURIResolver() {
    return RESOLVER != null ? (URIResolver) Reflect.get(RESOLVER, cm) : null;
  }

  /**
   * Returns an entity resolver.
   * @return entity resolver (can be {@code null})
   */
  public EntityResolver getEntityResolver() {
    return RESOLVER != null ? (EntityResolver) Reflect.get(RESOLVER, cm) : null;
  }

  /**
   * Returns a resource resolver.
   * @return resource resolver (can be {@code null})
   */
  public LSResourceResolver getResourceResolver() {
    return RESOURCERESOLVER != null ? (LSResourceResolver)
      Reflect.get(RESOURCERESOLVER, new Object[] { new String[] { catfile } }) : null;
  }
}
