package mat.server.service.impl;

import java.util.ArrayList;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * store an array of errors
 * @author aschmidt
 *
 */
public class XMLErrorHandler implements ErrorHandler {

	ArrayList<String> errors = new ArrayList<String>();

	@Override
	public void error(SAXParseException arg0) throws SAXException {
		createError(arg0);
	}

	@Override
	public void fatalError(SAXParseException arg0) throws SAXException {
		createError(arg0);
	}

	@Override
	public void warning(SAXParseException arg0) throws SAXException {
		createError(arg0);
	}

	private void createError(SAXParseException arg0) {
		errors.add(arg0.getMessage());
	}
	public ArrayList<String> getErrors() {
		return errors;
	}
}
