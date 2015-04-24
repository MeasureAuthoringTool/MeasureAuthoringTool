package mat.client.resource;
import com.google.gwt.user.cellview.client.CellTable;

/** The Interface CellTableResource. */
public interface CellTableResource extends CellTable.Resources {
	
	/** The Interface CellTableStyle. */
	public interface CellTableStyle extends CellTable.Style { };
	
	@Override
	@Source(value = {CellTable.Style.DEFAULT_CSS, "CellTable.css" })
	CellTableStyle cellTableStyle();
};
