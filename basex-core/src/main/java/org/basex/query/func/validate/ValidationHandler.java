package org.basex.query.func.validate;

import java.io.*;
import java.util.*;

import org.basex.build.xml.*;
import org.basex.io.*;
import org.basex.query.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * Error handler.
 *
 * @author BaseX Team 2005-19, BSD License
 * @author Christian Gruen
 */
public final class ValidationHandler extends DefaultHandler {
  /** Fatal error. */
  static final String FATAL = "Fatal";
  /** Error. */
  static final String ERROR = "Error";
  /** Warning. */
  static final String WARNING = "Warning";

  /** Errors. */
  private final ArrayList<ErrorInfo> errors = new ArrayList<>();
  /** Entity resolver (can be {@code null}). */
  private final EntityResolver resolver;

  /**
   * Constructor.
   * @param qc query context
   */
  ValidationHandler(final QueryContext qc) {
    resolver = new CatalogWrapper(qc.context).getEntityResolver();
  }

  /** Schema URL. */
  private IO schema;

  @Override
  public InputSource resolveEntity(final String publicId, final String systemId)
      throws IOException, SAXException {
    return resolver != null ? resolver.resolveEntity(publicId, systemId) : null;
  }

  @Override
  public void fatalError(final SAXParseException ex) {
    add(ex, FATAL);
  }

  @Override
  public void error(final SAXParseException ex) {
    add(ex, ERROR);
  }

  @Override
  public void warning(final SAXParseException ex) {
    add(ex, WARNING);
  }

  /**
   * Adds a new error info.
   * @param ex exception
   * @param level level
   */
  void add(final SAXException ex, final String level) {
    errors.add(new ErrorInfo(ex, level, schema));
  }

  /**
   * Assigns the schema reference.
   * @param io schema reference
   */
  void schema(final IO io) {
    schema = io;
  }

  /**
   * Returns the errors.
   * @return errors
   */
  ArrayList<ErrorInfo> getErrors() {
    return errors;
  }
}
