package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLError implements IsSerializable, Comparable<CQLError> {

    public static final String ERROR_SEVERITY = "Error";
    public static final String SEVERE_SEVERITY = "Severe";

    private int errorInLine;
    private int errorAtOffeset;

    private int startErrorInLine;
    private int startErrorAtOffset;

    private int endErrorInLine;
    private int endErrorAtOffset;

    private String errorMessage;

    private String severity;


    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public int getErrorInLine() {
        return errorInLine;
    }

    public void setErrorInLine(int errorInLine) {
        this.errorInLine = errorInLine;
    }

    public int getErrorAtOffeset() {
        return errorAtOffeset;
    }

    public void setErrorAtOffset(int errorAtOffeset) {
        this.errorAtOffeset = errorAtOffeset;
    }

    @Override
    public String toString() {
        return this.errorInLine + ";" + this.errorAtOffeset + ":" + this.errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getStartErrorInLine() {
        return startErrorInLine;
    }

    public void setStartErrorInLine(int startErrorInLine) {
        this.startErrorInLine = startErrorInLine;
    }

    public int getStartErrorAtOffset() {
        return startErrorAtOffset;
    }

    public void setStartErrorAtOffset(int startErrorAtOffset) {
        this.startErrorAtOffset = startErrorAtOffset;
    }

    public int getEndErrorInLine() {
        return endErrorInLine;
    }

    public void setEndErrorInLine(int endErrorInLine) {
        this.endErrorInLine = endErrorInLine;
    }

    public int getEndErrorAtOffset() {
        return endErrorAtOffset;
    }

    public void setEndErrorAtOffset(int endErrorAtOffset) {
        this.endErrorAtOffset = endErrorAtOffset;
    }

    @Override
    public int compareTo(CQLError o) {

        if (this.errorInLine > o.errorInLine) {
            return 1;
        } else if (this.errorInLine < o.errorInLine) {
            return -1;
        }

        return 0;
    }

}
