package mat.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class HarpSupportWidget extends Composite {

    private static final HarpSupportWidgetBinder uiBinder = GWT.create(HarpSupportWidgetBinder.class);

    public HarpSupportWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    interface HarpSupportWidgetBinder extends UiBinder<Widget, HarpSupportWidget> {
    }

}
