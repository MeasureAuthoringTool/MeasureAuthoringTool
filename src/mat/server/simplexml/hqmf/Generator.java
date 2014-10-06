package mat.server.simplexml.hqmf;

import mat.model.clause.MeasureExport;

public interface Generator {

	public abstract String generate(MeasureExport me);

}