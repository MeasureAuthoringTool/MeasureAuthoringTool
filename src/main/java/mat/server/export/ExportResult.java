package mat.server.export;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExportResult {
	private String cqlLibraryName;

	public String measureName;

	private String valueSetName;

	public String packageDate;

	public String export;

	public List<ExportResult> includedCQLExports = new ArrayList<>();

	public byte[] wkbkbarr;

	public byte[] zipbarr;

	private String lastModifiedDate;

	private String cqlLibraryVersion;

	private String cqlLibraryModelVersion;
}
