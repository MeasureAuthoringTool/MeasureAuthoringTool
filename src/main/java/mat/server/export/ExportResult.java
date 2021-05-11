package mat.server.export;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExportResult {
	private String cqlLibraryName;

	private String measureName;

	private String valueSetName;

    private String packageDate;

    private String export;

    private List<ExportResult> includedCQLExports = new ArrayList<>();

    private byte[] wkbkbarr;

    private byte[] zipbarr;

    private String lastModifiedDate;

    private String cqlLibraryVersion;

    private String cqlLibraryModelVersion;
}
