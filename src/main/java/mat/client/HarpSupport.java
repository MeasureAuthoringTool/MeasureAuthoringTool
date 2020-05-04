package mat.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Panel;
import mat.client.shared.MatContext;
import mat.shared.ConstantMessages;

public class HarpSupport extends MainLayout implements EntryPoint {

    private final HarpSupportWidget supportWidget = GWT.create(HarpSupportWidget.class);

    @Override
    protected void initEntryPoint() {
        MatContext.get().setCurrentModule(ConstantMessages.HARP_SUPPORT_MODULE);
        Panel content = getContentPanel();
        content.add(supportWidget);
    }

}
