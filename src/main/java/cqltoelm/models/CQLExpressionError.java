package cqltoelm.models;

import org.cqframework.cql.cql2elm.CqlTranslatorException;

/**
 * @author Jack Meyer
 *
 * The CQL Expression Error. This error represents a simplified version of the CqlException class.
 */
public class CQLExpressionError {

    /**
     * The error message
     */
    private String message;

    /**
     * The start line
     */
    private int startLine;

    /**
     * The end line
     */
    private int endLine;

    /**
     * The start character
     */
    private int startChar;

    /**
     * The end character
     */
    private int endChar;

    private CqlTranslatorException.ErrorSeverity severity;

    public CQLExpressionError(String message, int startLine, int endLine, int startChar, int endChar, CqlTranslatorException.ErrorSeverity severity) {
        this.message = message;
        this.startLine = startLine;
        this.endLine = endLine;
        this.startChar = startChar;
        this.endChar = endChar;
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public int getStartChar() {
        return startChar;
    }

    public void setStartChar(int startChar) {
        this.startChar = startChar;
    }

    public int getEndChar() {
        return endChar;
    }

    public void setEndChar(int endChar) {
        this.endChar = endChar;
    }

    public CqlTranslatorException.ErrorSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(CqlTranslatorException.ErrorSeverity severity) {
        this.severity = severity;
    }

    @Override
    public String toString() {
        return "CQLExpressionError{" +
                "message='" + message + '\'' +
                ", startLine=" + startLine +
                ", endLine=" + endLine +
                ", startChar=" + startChar +
                ", endChar=" + endChar +
                ", severity=" + severity +
                '}';
    }
}
