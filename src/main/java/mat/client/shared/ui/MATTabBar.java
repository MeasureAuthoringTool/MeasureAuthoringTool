/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package mat.client.shared.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Accessibility;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWordWrap;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import mat.client.shared.MatContext;

/**
 * A horizontal bar of folder-style tabs, most commonly used as part of a.
 * <p>
 * {@link com.google.gwt.user.client.ui.TabPanel}.
 * <p>
 * <img class='gallery' src='doc-files/TabBar.png'/>
 * </p>
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.gwt-TabBar { the tab bar itself }</li>
 * <li>.gwt-TabBar .gwt-TabBarFirst { the left edge of the bar }</li>
 * <li>.gwt-TabBar .gwt-TabBarFirst-wrapper { table cell around the left edge }</li>
 * <li>.gwt-TabBar .gwt-TabBarRest { the right edge of the bar }</li>
 * <li>.gwt-TabBar .gwt-TabBarRest-wrapper { table cell around the right edge }</li>
 * <li>.gwt-TabBar .gwt-TabBarItem { unselected tabs }</li>
 * <li>.gwt-TabBar .gwt-TabBarItem-wrapper { table cell around tab }</li>
 * <li>.gwt-TabBar .gwt-TabBarItem-selected { additional style for selected
 * <p>
 * <h3>Example</h3>
 * {@example com.google.gwt.examples.TabBarExample}
 * </p>
 */

/**
 * Almost an exact implementation of the GWT TabBar class
 * see method onBrowserEvent in inner class: ClickDelegatePanel
 * Our MAT need was to hijack the tab selection (mouse click or key) 
 * and output an alert if a check fails   
 * revision by aschmidt
 *
 */
public class MATTabBar extends Composite implements SourcesTabEvents,
        HasBeforeSelectionHandlers<Integer>, HasSelectionHandlers<Integer>,
        ClickListener, KeyboardListener {

    /**
     * Set of characteristic interfaces supported by {@link TabBar} tabs.
     *
     * Note that this set might expand over time, so implement this interface at
     * your own risk.
     */
    public interface Tab extends HasAllKeyHandlers, HasClickHandlers, HasWordWrap {
        /**
         * Check if the underlying widget implements {@link HasWordWrap}.
         *
         * @return true if the widget implements {@link HasWordWrap}
         */
        boolean hasWordWrap();
    }

    /**
     * <code>ClickDelegatePanel</code> decorates any widget with the minimal
     * amount of machinery to receive clicks for delegation to the parent.
     * {@link SourcesClickEvents} is not implemented due to the fact that only a
     * single observer is needed.
     */
    private class ClickDelegatePanel extends Composite implements Tab {

        /** The focusable panel. */
        private FocusPanel focusablePanel;

        /** The enabled. */
        private boolean enabled = true;

        /**
         * Instantiates a new click delegate panel.
         *
         * @param child
         *            the child
         */
        ClickDelegatePanel(Widget child) {

            focusablePanel = new FocusPanel(child);
            SimplePanel wrapperWidget = createTabTextWrapper();
            if (wrapperWidget == null) {
                initWidget(focusablePanel);
            } else {
                wrapperWidget.setWidget(focusablePanel);
                initWidget(wrapperWidget);
            }

            sinkEvents(Event.ONCLICK | Event.ONKEYDOWN);
        }

        /* (non-Javadoc)
         * @see com.google.gwt.event.dom.client.HasClickHandlers#addClickHandler(com.google.gwt.event.dom.client.ClickHandler)
         */
        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return addHandler(handler, ClickEvent.getType());
        }

        /* (non-Javadoc)
         * @see com.google.gwt.event.dom.client.HasKeyDownHandlers#addKeyDownHandler(com.google.gwt.event.dom.client.KeyDownHandler)
         */
        public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
            return addHandler(handler, KeyDownEvent.getType());
        }

        /* (non-Javadoc)
         * @see com.google.gwt.event.dom.client.HasKeyPressHandlers#addKeyPressHandler(com.google.gwt.event.dom.client.KeyPressHandler)
         */
        public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
            return addDomHandler(handler, KeyPressEvent.getType());
        }

        /* (non-Javadoc)
         * @see com.google.gwt.event.dom.client.HasKeyUpHandlers#addKeyUpHandler(com.google.gwt.event.dom.client.KeyUpHandler)
         */
        public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
            return addDomHandler(handler, KeyUpEvent.getType());
        }

        /**
         * Gets the focusable panel.
         *
         * @return the focusable panel
         */
        public SimplePanel getFocusablePanel() {
            return focusablePanel;
        }

        /* (non-Javadoc)
         * @see com.google.gwt.user.client.ui.HasWordWrap#getWordWrap()
         */
        public boolean getWordWrap() {
            if (hasWordWrap()) {
                return ((HasWordWrap) focusablePanel.getWidget()).getWordWrap();
            }
            throw new UnsupportedOperationException(
                    "Widget does not implement HasWordWrap");
        }

        /* (non-Javadoc)
         * @see mat.client.shared.ui.MATTabBar.Tab#hasWordWrap()
         */
        public boolean hasWordWrap() {
            return focusablePanel.getWidget() instanceof HasWordWrap;
        }

        /**
         * Checks if is enabled.
         *
         * @return true, if is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * NOTE pass on browser event only if the loading check passes else show an
         * alert message.
         *
         * @param event
         *            the event
         */
        @Override
        public void onBrowserEvent(Event event) {

            if (!enabled) {
                return;
            }
            if (!MatContext.get().isLoading()) {
                // No need for call to super.
                switch (DOM.eventGetType(event)) {
                    case Event.ONCLICK:
                        selectTabByTabWidget(this);
                        onClick(this);
                        break;

                    case Event.ONKEYDOWN:
                        if (((char) DOM.eventGetKeyCode(event)) == KeyCodes.KEY_ENTER) {
                            selectTabByTabWidget(this);
                        }
                        onKeyDown(this, (char) event.getKeyCode(),
                                KeyboardListenerCollection.getKeyboardModifiers(event));
                        break;
                }
                super.onBrowserEvent(event);
            } else {
                MatContext.get().fireLoadingAlert();
            }
        }

        /**
         * Sets the enabled.
         *
         * @param enabled
         *            the new enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        /* (non-Javadoc)
         * @see com.google.gwt.user.client.ui.HasWordWrap#setWordWrap(boolean)
         */
        public void setWordWrap(boolean wrap) {
            if (hasWordWrap()) {
                ((HasWordWrap) focusablePanel.getWidget()).setWordWrap(wrap);
            } else {
                throw new UnsupportedOperationException(
                        "Widget does not implement HasWordWrap");
            }
        }
    }

    /** The Constant STYLENAME_DEFAULT. */
    private static final String STYLENAME_DEFAULT = "gwt-TabBarItem";

    /** The panel. */
    private HorizontalPanel panel = new HorizontalPanel();

    /** The selected tab. */
    private Widget selectedTab;

    /**
     * Creates an empty tab bar.
     */
    public MATTabBar() {
        panel.getElement().setId("panel_HorizontalPanel");
        initWidget(panel);
        sinkEvents(Event.ONCLICK);
        setStyleName("gwt-TabBar");

        // Add a11y role "tablist"
        Accessibility.setRole(panel.getElement(), Accessibility.ROLE_TABLIST);

        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

        HTML first = new HTML("&nbsp;", true), rest = new HTML("&nbsp;", true);
        first.setStyleName("gwt-TabBarFirst");
        rest.setStyleName("gwt-TabBarRest");
        first.setHeight("100%");
        rest.setHeight("100%");

        panel.add(first);
        panel.add(rest);
        first.setHeight("100%");
        panel.setCellHeight(first, "100%");
        panel.setCellWidth(rest, "100%");
        setStyleName(first.getElement().getParentElement(),
                "gwt-TabBarFirst-wrapper");
        setStyleName(rest.getElement().getParentElement(), "gwt-TabBarRest-wrapper");
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers#addBeforeSelectionHandler(com.google.gwt.event.logical.shared.BeforeSelectionHandler)
     */
    public HandlerRegistration addBeforeSelectionHandler(
            BeforeSelectionHandler<Integer> handler) {
        return addHandler(handler, BeforeSelectionEvent.getType());
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.logical.shared.HasSelectionHandlers#addSelectionHandler(com.google.gwt.event.logical.shared.SelectionHandler)
     */
    public HandlerRegistration addSelectionHandler(
            SelectionHandler<Integer> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    /**
     * Adds a new tab with the specified text.
     *
     * @param text the new tab's text
     */
    public void addTab(String text, boolean isVisible) {
        insertTab(text, getTabCount(), isVisible);
    }

    /**
     * Adds a new tab with the specified text.
     *
     * @param text the new tab's text
     * @param asHTML <code>true</code> to treat the specified text as html
     */
    public void addTab(String text, boolean asHTML, boolean isVisible) {
        insertTab(text, asHTML, getTabCount(), isVisible);
    }

    /**
     * Adds a new tab with the specified widget.
     *
     * @param widget the new tab's widget
     */
    public void addTab(Widget widget, boolean isVisible) {
        insertTab(widget, getTabCount(), isVisible);
    }

    /**
     * Adds the tab listener.
     *
     * @param listener
     *            the listener
     * @deprecated Use {@link #addBeforeSelectionHandler} and {#link
     *             #addSelectionHandler} instead
     */
    @Deprecated
    public void addTabListener(TabListener listener) {
        MATListenerWrapper.WrappedTabListener.add(this, listener);
    }

    /**
     * Gets the tab that is currently selected.
     *
     * @return the selected tab
     */
    public int getSelectedTab() {
        if (selectedTab == null) {
            return -1;
        }
        return panel.getWidgetIndex(selectedTab) - 1;
    }

    /**
     * Gets the given tab.
     *
     * This method is final because the Tab interface will expand. Therefore
     * it is highly likely that subclasses which implemented this method would end up
     * breaking.
     *
     * @param index the tab's index
     * @return the tab wrapper
     */
    public final Tab getTab(int index) {
        if (index >= getTabCount()) {
            return null;
        }
        ClickDelegatePanel p = (ClickDelegatePanel) panel.getWidget(index + 1);
        return p;
    }

    /**
     * Gets the number of tabs present.
     *
     * @return the tab count
     */
    public int getTabCount() {
        return panel.getWidgetCount() - 2;
    }

    /**
     * Gets the specified tab's HTML.
     *
     * @param index the index of the tab whose HTML is to be retrieved
     * @return the tab's HTML
     */
    public String getTabHTML(int index) {
        if (index >= getTabCount()) {
            return null;
        }
        ClickDelegatePanel delPanel = (ClickDelegatePanel) panel.getWidget(index + 1);
        SimplePanel focusablePanel = delPanel.getFocusablePanel();
        Widget widget = focusablePanel.getWidget();
        if (widget instanceof HTML) {
            return ((HTML) widget).getHTML();
        } else if (widget instanceof Label) {
            return ((Label) widget).getText();
        } else {
            // This will be a focusable panel holding a user-supplied widget.
            return focusablePanel.getElement().getParentElement().getInnerHTML();
        }
    }

    /**
     * Inserts a new tab at the specified index.
     *
     * @param text the new tab's text
     * @param asHTML <code>true</code> to treat the specified text as HTML
     * @param beforeIndex the index before which this tab will be inserted
     */
    public void insertTab(String text, boolean asHTML, int beforeIndex, boolean isVisible) {
        checkInsertBeforeTabIndex(beforeIndex);

        Label item;
        if (asHTML) {
            item = new HTML(text);
        } else {
            item = new Label(text);
        }

        item.setWordWrap(false);
        insertTabWidget(item, beforeIndex, text, isVisible);
    }

    /**
     * Inserts a new tab at the specified index.
     *
     * @param text the new tab's text
     * @param beforeIndex the index before which this tab will be inserted
     */
    public void insertTab(String text, int beforeIndex, boolean isVisible) {
        insertTab(text, false, beforeIndex, isVisible);
    }

    /**
     * Inserts a new tab at the specified index.
     *
     * @param widget widget to be used in the new tab
     * @param beforeIndex the index before which this tab will be inserted
     */
    public void insertTab(Widget widget, int beforeIndex, boolean isVisible) {
        insertTabWidget(widget, beforeIndex, null, isVisible);
    }

    /**
     * Check if a tab is enabled or disabled. If disabled, the user cannot select
     * the tab.
     *
     * @param index the index of the tab
     * @return true if the tab is enabled, false if disabled
     */
    public boolean isTabEnabled(int index) {
        assert (index >= 0) && (index < getTabCount()) : "Tab index out of bounds";
        ClickDelegatePanel delPanel = (ClickDelegatePanel) panel.getWidget(index + 1);
        return delPanel.isEnabled();
    }

    /**
     * On click.
     *
     * @param sender
     *            the sender
     * @deprecated add a {@link BeforeSelectionHandler} instead. Alternatively,
     *             if you need to access to the individual tabs, add a click
     *             handler to each {@link Tab} element instead.
     */
    @Deprecated
    public void onClick(Widget sender) {
    }

    /**
     * On key down.
     *
     * @param sender
     *            the sender
     * @param keyCode
     *            the key code
     * @param modifiers
     *            the modifiers
     * @deprecated add a key down handler to the individual {@link Tab} objects
     *             instead.
     */
    @Deprecated
    public void onKeyDown(Widget sender, char keyCode, int modifiers) {
    }

    /**
     * On key press.
     *
     * @param sender
     *            the sender
     * @param keyCode
     *            the key code
     * @param modifiers
     *            the modifiers
     * @deprecated this method has been doing nothing for the entire last
     *             release, if what you wanted to do was to listen to key press
     *             events on tabs, add the key press handler to the individual
     *             tab wrappers instead.
     */
    @Deprecated
    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
    }

    /**
     * On key up.
     *
     * @param sender
     *            the sender
     * @param keyCode
     *            the key code
     * @param modifiers
     *            the modifiers
     * @deprecated this method has been doing nothing for the entire last
     *             release, if what you wanted to do was to listen to key up
     *             events on tabs, add the key up handler to the individual tab
     *             wrappers instead.
     */
    @Deprecated
    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
    }

    /**
     * Removes the tab at the specified index.
     *
     * @param index the index of the tab to be removed
     */
    public void removeTab(int index) {
        checkTabIndex(index);

        // (index + 1) to account for 'first' placeholder widget.
        Widget toRemove = panel.getWidget(index + 1);
        if (toRemove == selectedTab) {
            selectedTab = null;
        }
        panel.remove(toRemove);
    }

    /**
     * Removes the tab listener.
     *
     * @param listener
     *            the listener
     * @deprecated Instead use the {@link HandlerRegistration#removeHandler}
     *             call on the object returned by an add*Handler method
     */
    @Deprecated
    public void removeTabListener(TabListener listener) {
        MATListenerWrapper.WrappedTabListener.remove(this, listener);
    }

    /**
     * Programmatically selects the specified tab. Use index -1 to specify that no
     * tab should be selected.
     *
     * @param index the index of the tab to be selected
     * @return <code>true</code> if successful, <code>false</code> if the change
     * is denied by the {@link BeforeSelectionHandler}.
     */
    public boolean selectTab(int index) {
        checkTabIndex(index);
        BeforeSelectionEvent<?> event = BeforeSelectionEvent.fire(this, index);

        if (event != null && event.isCanceled()) {
            return false;
        }

        // Check for -1.
        setSelectionStyle(selectedTab, false);
        if (index == -1) {
            selectedTab = null;
            return true;
        }
        if (!MatContext.get().isErrorTab()) {// if there is any error to be thrown, do not fire Event.
            selectedTab = panel.getWidget(index + 1);
            setSelectionStyle(selectedTab, true);
            SelectionEvent.fire(this, index);
        } else {
            selectedTab = panel.getWidget(MatContext.get().getErrorTabIndex() + 1);
            setSelectionStyle(selectedTab, true);
            MatContext.get().setErrorTab(false);
        }
        return true;
    }

    /**
     * Enable or disable a tab. When disabled, users cannot select the tab.
     *
     * @param index the index of the tab to enable or disable
     * @param enabled true to enable, false to disable
     */
    public void setTabEnabled(int index, boolean enabled) {
        assert (index >= 0) && (index < getTabCount()) : "Tab index out of bounds";

        // Style the wrapper
        ClickDelegatePanel delPanel = (ClickDelegatePanel) panel.getWidget(index + 1);
        delPanel.setEnabled(enabled);
        setStyleName(delPanel.getElement(), "gwt-TabBarItem-disabled", !enabled);
        setStyleName(delPanel.getElement().getParentElement(),
                "gwt-TabBarItem-wrapper-disabled", !enabled);
    }


    /**
     * Sets a tab's contents via HTML.
     *
     * Use care when setting an object's HTML; it is an easy way to expose
     * script-based security problems. Consider using
     *
     * @param index
     *            the index of the tab whose HTML is to be set
     * @param html
     *            the tab new HTML {@link #setTabText(int, String)} whenever
     *            possible.
     */
    public void setTabHTML(int index, String html) {
        assert (index >= 0) && (index < getTabCount()) : "Tab index out of bounds";

        ClickDelegatePanel delPanel = (ClickDelegatePanel) panel.getWidget(index + 1);
        SimplePanel focusablePanel = delPanel.getFocusablePanel();
        focusablePanel.setWidget(new HTML(html, false));
    }

    /**
     * Sets a tab's text contents.
     *
     * @param index the index of the tab whose text is to be set
     * @param text the object's new text
     */
    public void setTabText(int index, String text) {
        assert (index >= 0) && (index < getTabCount()) : "Tab index out of bounds";

        ClickDelegatePanel delPanel = (ClickDelegatePanel) panel.getWidget(index + 1);
        SimplePanel focusablePanel = delPanel.getFocusablePanel();

        // It is not safe to check if the current widget is an instanceof Label and
        // reuse it here because HTML is an instanceof Label. Leaving an HTML would
        // throw off the results of getTabHTML(int).
        focusablePanel.setWidget(new Label(text, false));
    }

    /**
     * Create a {@link SimplePanel} that will wrap the contents in a tab.
     * Subclasses can use this method to wrap tabs in decorator panels.
     *
     * @return a {@link SimplePanel} to wrap the tab contents, or null to leave
     * tabs unwrapped
     */
    protected SimplePanel createTabTextWrapper() {
        return null;
    }

    /**
     * Inserts a new tab at the specified index.
     *
     * @param widget widget to be used in the new tab
     * @param beforeIndex the index before which this tab will be inserted
     */
    protected void insertTabWidget(Widget widget, int beforeIndex, String text, boolean isVisible) {
        checkInsertBeforeTabIndex(beforeIndex);

        ClickDelegatePanel delWidget = new ClickDelegatePanel(widget);
        delWidget.setStyleName(STYLENAME_DEFAULT);

        // Add a11y role "tab"
        SimplePanel focusablePanel = delWidget.getFocusablePanel();
        if (text != null) {
            delWidget.getFocusablePanel().getElement().setAttribute("id", text);
        }
        Accessibility.setRole(focusablePanel.getElement(), Accessibility.ROLE_TAB);

        panel.insert(delWidget, beforeIndex + 1);
        delWidget.setVisible(isVisible);
        setStyleName(DOM.getParent(delWidget.getElement()), STYLENAME_DEFAULT
                + "-wrapper", true);
    }

    /**
     * <b>Affected Elements:</b>
     * <ul>
     * <li>-tab# = The element containing the contents of the tab.</li>
     * <li>-tab-wrapper# = The cell containing the tab at the index.</li>
     * </ul>
     *
     * @param baseID
     *            the base id
     * @see UIObject#onEnsureDebugId(String)
     */
    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        int numTabs = getTabCount();
        for (int i = 0; i < numTabs; i++) {
            ClickDelegatePanel delPanel = (ClickDelegatePanel) panel.getWidget(i + 1);
            SimplePanel focusablePanel = delPanel.getFocusablePanel();
            ensureDebugId(focusablePanel.getElement(), baseID, "tab" + i);
            ensureDebugId(DOM.getParent(delPanel.getElement()), baseID, "tab-wrapper"
                    + i);
        }
    }

    /**
     * Check insert before tab index.
     *
     * @param beforeIndex
     *            the before index
     */
    private void checkInsertBeforeTabIndex(int beforeIndex) {
        if ((beforeIndex < 0) || (beforeIndex > getTabCount())) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Check tab index.
     *
     * @param index
     *            the index
     */
    private void checkTabIndex(int index) {
        if ((index < -1) || (index >= getTabCount())) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Selects the tab corresponding to the widget for the tab. To be clear the
     * widget for the tab is not the widget INSIDE of the tab; it is the widget
     * used to represent the tab itself.
     *
     * @param tabWidget The widget for the tab to be selected
     * @return true if the tab corresponding to the widget for the tab could
     * located and selected, false otherwise
     */
    private boolean selectTabByTabWidget(Widget tabWidget) {
        int numTabs = panel.getWidgetCount() - 1;

        for (int i = 1; i < numTabs; ++i) {
            if (panel.getWidget(i) == tabWidget) {
                return selectTab(i - 1);
            }
        }

        return false;
    }

    /**
     * Sets the selection style.
     *
     * @param item
     *            the item
     * @param selected
     *            the selected
     */
    private void setSelectionStyle(Widget item, boolean selected) {
        if (item != null) {
            for (int i = 1; i < panel.getWidgetCount(); i++) {
                Widget w = panel.getWidget(i);
                if (w != item) {
                    w.getElement().setAttribute("aria-selected","false");
                }
            }

            if (selected) {
                item.addStyleName("gwt-TabBarItem-selected");
                item.getElement().setAttribute("aria-selected","true");
                setStyleName(DOM.getParent(item.getElement()),
                        "gwt-TabBarItem-wrapper-selected", true);
            } else {
                item.removeStyleName("gwt-TabBarItem-selected");
                item.getElement().setAttribute("aria-selected","false");
                setStyleName(DOM.getParent(item.getElement()),
                        "gwt-TabBarItem-wrapper-selected", false);
            }
        }
    }
}
