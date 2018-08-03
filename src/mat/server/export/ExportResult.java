package mat.server.export;

import java.util.ArrayList;
import java.util.List;

public class ExportResult {
	private String cqlLibraryName;

	/** The measure name. */
	public String measureName;

	/** The value set name. */
	public String valueSetName;

	/** The package date. */
	public String packageDate;

	/** The export. */
	public String export;

	public List<ExportResult> includedCQLExports = new ArrayList<ExportResult>();

	/** The wkbkbarr. */
	public byte[] wkbkbarr;

	/** The zipbarr. */
	public byte[] zipbarr;

	/** The last modified date. */
	public String lastModifiedDate;

	public String getCqlLibraryName() {
		return cqlLibraryName;
	}

	public void setCqlLibraryName(String cqlLibraryName) {
		this.cqlLibraryName = cqlLibraryName;
	}

	public List<ExportResult> getIncludedCQLExports() {
		return includedCQLExports;
	}
}
