package mat.server.service.impl;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;

/**
 * store an array of errors.
 * 
 * @author aschmidt
 */
public class XMLErrorHandler implements ErrorHandler {

	/** The errors. */
	ArrayList<String> errors = new ArrayList<String>();

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */
	@Override
	public void error(SAXParseException arg0) throws SAXException {
		createError(arg0);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	@Override
	public void fatalError(SAXParseException arg0) throws SAXException {
		createError(arg0);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */
	@Override
	public void warning(SAXParseException arg0) throws SAXException {
		createError(arg0);
	}

	/**
	 * Creates the error.
	 * 
	 * @param arg0
	 *            the arg0
	 */
	private void createError(SAXParseException arg0) {
		errors.add(arg0.getMessage());
	}
	
	/**
	 * Gets the errors.
	 * 
	 * @return the errors
	 */
	public ArrayList<String> getErrors() {
		return errors;
	}
}
