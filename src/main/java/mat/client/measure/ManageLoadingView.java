package mat.client.measure;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.ImageResources;
import mat.client.shared.MatContext;
import mat.client.util.ClientConstants;

/**
 * The Class ManageLoadingView.
 */
public class ManageLoadingView {

    /**
     * The loading panel.
     */
    private static Panel loadingPanel;

    /**
     * The loading widget.
     */
    private static HTML loadingWidget = new HTML(ClientConstants.MAINLAYOUT_LOADING_WIDGET_MSG);

    /**
     * The alert image.
     */
    private static Image alertImage = new Image(ImageResources.INSTANCE.alert());

    /**
     * The alert title.
     */
    private static String alertTitle = ClientConstants.MAINLAYOUT_ALERT_TITLE;

    /**
     * The Constant DEFAULT_LOADING_MSAGE_DELAY_IN_MILLISECONDS.
     */
    private static final int DEFAULT_LOADING_MSAGE_DELAY_IN_MILLISECONDS = 10000;

    /**
     * Builds the loading panel.
     *
     * @param id the id
     * @return the panel
     */
    static Panel buildLoadingPanel(String id) {
        loadingPanel = new HorizontalPanel();
        loadingPanel.getElement().setAttribute("id", id);
        loadingPanel.getElement().setAttribute("aria-role", "loadingwidget");
        loadingPanel.getElement().setAttribute("aria-labelledby", "LiveRegion");
        loadingPanel.getElement().setAttribute("aria-live", "assertive");
        loadingPanel.getElement().setAttribute("aria-atomic", "true");
        loadingPanel.getElement().setAttribute("aria-relevant", "all");

        loadingPanel.setStylePrimaryName("mainContentPanel");
        setId(loadingPanel, id);
        alertImage.setTitle(alertTitle);
        alertImage.getElement().setAttribute("alt", alertTitle);
        loadingWidget.setStyleName("padLeft5px");
        return loadingPanel;
    }

    /**
     * Sets the id.
     *
     * @param widget the widget
     * @param id     the id
     */
    protected static void setId(final Widget widget, final String id) {
        DOM.setElementAttribute(widget.getElement(), "id", id);
    }

    /**
     * Show loading message.
     */
    public static void showLoadingMessage() {
        getLoadingPanel().clear();
        getLoadingPanel().add(alertImage);
        getLoadingPanel().add(loadingWidget);
        getLoadingPanel().setStyleName("msg-alert");
        getLoadingPanel().getElement().setAttribute("role", "alert");
        MatContext.get().getLoadingQueue().add("node");
    }

    /**
     * Gets the loading panel.
     *
     * @return the loadingPanel
     */
    public static Panel getLoadingPanel() {
        return loadingPanel;
    }

    /**
     * Sets the loading panel.
     *
     * @param loadingPanel the loadingPanel to set
     */
    public static void setLoadingPanel(Panel loadingPanel) {
        ManageLoadingView.loadingPanel = loadingPanel;
    }

    /**
     * no arg method adds default delay to loading message hide op.
     */
    public static void hideLoadingMessage() {
        hideLoadingMessage(DEFAULT_LOADING_MSAGE_DELAY_IN_MILLISECONDS);
    }

    /**
     * delay hiding of loading message artifacts by 'delay' milliseconds NOTE
     * delay cannot be <= 0 else exception is thrown public method to allow
     * setting of delay.
     *
     * @param delay the delay
     */
    public static void hideLoadingMessage(final int delay) {
        if (delay > 0) {
            final Timer timer = new Timer() {
                @Override
                public void run() {
                    delegateHideLoadingMessage();
                }
            };
            timer.schedule(delay);
        } else {
            delegateHideLoadingMessage();
        }
    }

    /**
     * clear the loading panel remove css style reset the loading queue.
     */
    private static void delegateHideLoadingMessage() {
        MatContext.get().getLoadingQueue().poll();
        if (MatContext.get().getLoadingQueue().size() == 0) {
            getLoadingPanel().clear();
            getLoadingPanel().remove(alertImage);
            getLoadingPanel().remove(loadingWidget);
            getLoadingPanel().removeStyleName("msg-alert");
            getLoadingPanel().getElement().removeAttribute("role");
        }
    }
}
