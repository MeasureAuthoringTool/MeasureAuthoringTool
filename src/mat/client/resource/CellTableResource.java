package mat.client.resource;
import com.google.gwt.user.cellview.client.CellTable;

public interface CellTableResource extends CellTable.Resources
{
   public interface CellTableStyle extends CellTable.Style {};

   @Source(value={CellTable.Style.DEFAULT_CSS,"CellTable.css"})
   CellTableStyle cellTableStyle();
}; 
