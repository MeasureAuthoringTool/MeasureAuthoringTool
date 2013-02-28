package mat.client.shared;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.Range;

public class MatSimplePager extends SimplePager {

    public MatSimplePager() {
        this.setRangeLimited(true);
    }

    public MatSimplePager(TextLocation location, Resources resources, boolean showFastForwardButton, int fastForwardRows, boolean showLastPageButton) {
        super(location, resources, showFastForwardButton, fastForwardRows, showLastPageButton);
        this.setRangeLimited(true);
    }

    public void setPageStart(int index) {

        if (this.getDisplay() != null) {
          Range range = getDisplay().getVisibleRange();
          int pageSize = range.getLength();
          if (!isRangeLimited() && getDisplay().isRowCountExact()) {
            index = Math.min(index, getDisplay().getRowCount() - pageSize);
          }
          index = Math.max(0, index);
          if (index != range.getStart()) {
            getDisplay().setVisibleRange(index, pageSize);
          }
        }  
      }
    }