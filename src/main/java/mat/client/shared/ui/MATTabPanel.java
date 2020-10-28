package mat.client.shared.ui;

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

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Accessibility;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.Enableable;

import java.util.Iterator;

/**
 * A panel that represents a tabbed set of pages, each of which contains another
 * widget. Its child widgets are shown as the user selects the various tabs
 * associated with them. The tabs can contain arbitrary HTML.
 * 
 * <p>
 * This widget will <em>only</em> work in quirks mode. If your application is in
 * Standards Mode, use {@link TabLayoutPanel} instead.
 * </p>
 * 
 * <p>
 * <img class='gallery' src='doc-files/TabPanel.png'/>
 * </p>
 * 
 * <p>
 * Note that this widget is not a panel per se, but rather a
 * {@link com.google.gwt.user.client.ui.Composite} that aggregates a
 * {@link com.google.gwt.user.client.ui.TabBar} and a
 * {@link com.google.gwt.user.client.ui.DeckPanel}. It does, however, implement
 * {@link com.google.gwt.user.client.ui.HasWidgets}.
 * </p>
 * 
 * <h3>CSS Style Rules</h3> 
 * <ul class='css'> 
 * <li>.gwt-TabPanel { the tab panel itself }</li> 
 * <li>.gwt-TabPanelBottom { the bottom section of the tab panel
 * (the deck containing the widget) }</li> 
 * </ul>
 * 
 * <p>
 * <h3>Example</h3>
 * {@example com.google.gwt.examples.TabPanelExample}
 * </p>
 * 
 * @see TabLayoutPanel
 */

//Cannot do anything about tab panel implementing TabListener until next release.
public class MATTabPanel extends Composite implements TabListener,
    SourcesTabEvents, HasWidgets, HasAnimation, IndexedPanel,
    HasBeforeSelectionHandlers<Integer>, HasSelectionHandlers<Integer>, Enableable {
  /**
   * This extension of DeckPanel overrides the public mutator methods to prevent
   * external callers from adding to the state of the DeckPanel.
   * <p>
   * Removal of Widgets is supported so that WidgetCollection.WidgetIterator
   * operates as expected.
   * </p>
   * <p>
   * We ensure that the DeckPanel cannot become of of sync with its associated
   * TabBar by delegating all mutations to the TabBar to this implementation of
   * DeckPanel.
   * </p>
   */
  private static class TabbedDeckPanel extends DeckPanel {
    
    /** The tab bar. */
    private UnmodifiableTabBar tabBar;
    
    /**
	 * Instantiates a new tabbed deck panel.
	 * 
	 * @param tabBar
	 *            the tab bar
	 */
    public TabbedDeckPanel(UnmodifiableTabBar tabBar) {
      this.tabBar = tabBar;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.DeckPanel#add(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public void add(Widget w) {
      throw new UnsupportedOperationException(
          "Use TabPanel.add() to alter the DeckPanel");
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Panel#clear()
     */
    @Override
    public void clear() {
      throw new UnsupportedOperationException(
          "Use TabPanel.clear() to alter the DeckPanel");
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.DeckPanel#insert(com.google.gwt.user.client.ui.Widget, int)
     */
    @Override
    public void insert(Widget w, int beforeIndex) {
      throw new UnsupportedOperationException(
          "Use TabPanel.insert() to alter the DeckPanel");
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.DeckPanel#remove(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public boolean remove(Widget w) {
      // Removal of items from the TabBar is delegated to the DeckPanel
      // to ensure consistency
      int idx = getWidgetIndex(w);
      if (idx != -1) {
        tabBar.removeTabProtected(idx);
        return super.remove(w);
      }

      return false;
    }

    /**
	 * Insert protected.
	 * 
	 * @param w
	 *            the w
	 * @param tabText
	 *            the tab text
	 * @param asHTML
	 *            the as html
	 * @param beforeIndex
	 *            the before index
	 */
    protected void insertProtected(Widget w, String tabText, boolean asHTML,
        int beforeIndex, boolean isVisible) {

      // Check to see if the TabPanel already contains the Widget. If so,
      // remove it and see if we need to shift the position to the left.
      int idx = getWidgetIndex(w);
      if (idx != -1) {
        remove(w);
        if (idx < beforeIndex) {
          beforeIndex--;
        }
      }

      tabBar.insertTabProtected(tabText, asHTML, beforeIndex, isVisible);
      super.insert(w, beforeIndex);
    }

    /**
	 * Insert protected.
	 * 
	 * @param w
	 *            the w
	 * @param tabWidget
	 *            the tab widget
	 * @param beforeIndex
	 *            the before index
	 */
    protected void insertProtected(Widget w, Widget tabWidget, int beforeIndex, boolean isVisible) {

      // Check to see if the TabPanel already contains the Widget. If so,
      // remove it and see if we need to shift the position to the left.
      int idx = getWidgetIndex(w);
      if (idx != -1) {
        remove(w);
        if (idx < beforeIndex) {
          beforeIndex--;
        }
      }

      tabBar.insertTabProtected(tabWidget, beforeIndex, isVisible);
      super.insert(w, beforeIndex);
    }
  }

  /**
   * This extension of TabPanel overrides the public mutator methods to prevent
   * external callers from modifying the state of the TabBar.
   */
  private class UnmodifiableTabBar extends MATTabBar {
	
    /* (non-Javadoc)
     * @see mat.client.shared.ui.MATTabBar#insertTab(java.lang.String, boolean, int)
     */
    @Override
    public void insertTab(String text, boolean asHTML, int beforeIndex, boolean isVisible) {
      throw new UnsupportedOperationException(
          "Use TabPanel.insert() to alter the TabBar");
    }

    /* (non-Javadoc)
     * @see mat.client.shared.ui.MATTabBar#insertTab(com.google.gwt.user.client.ui.Widget, int)
     */
    @Override
    public void insertTab(Widget widget, int beforeIndex, boolean isVisible) {
      throw new UnsupportedOperationException(
          "Use TabPanel.insert() to alter the TabBar");
    }

    /**
	 * Insert tab protected.
	 * 
	 * @param text
	 *            the text
	 * @param asHTML
	 *            the as html
	 * @param beforeIndex
	 *            the before index
	 */
    public void insertTabProtected(String text, boolean asHTML, int beforeIndex, boolean isVisible) {
      super.insertTab(text, asHTML, beforeIndex, isVisible);
    }

    /**
	 * Insert tab protected.
	 * 
	 * @param widget
	 *            the widget
	 * @param beforeIndex
	 *            the before index
	 */
    public void insertTabProtected(Widget widget, int beforeIndex, boolean isVisible) {
      super.insertTab(widget, beforeIndex, isVisible);
    }

    /* (non-Javadoc)
     * @see mat.client.shared.ui.MATTabBar#removeTab(int)
     */
    @Override
    public void removeTab(int index) {
      // It's possible for removeTab() to function correctly, but it's
      // preferable to have only TabbedDeckPanel.remove() be operable,
      // especially since TabBar does not export an Iterator over its values.
      throw new UnsupportedOperationException(
          "Use TabPanel.remove() to alter the TabBar");
    }

    /**
	 * Removes the tab protected.
	 * 
	 * @param index
	 *            the index
	 */
    public void removeTabProtected(int index) {
      super.removeTab(index);
    }

    /* (non-Javadoc)
     * @see mat.client.shared.ui.MATTabBar#createTabTextWrapper()
     */
    @Override
    protected SimplePanel createTabTextWrapper() {
      return MATTabPanel.this.createTabTextWrapper();
    }
    
  }

  /** The tab bar. */
  private UnmodifiableTabBar tabBar = new UnmodifiableTabBar();
  
  /** The deck. */
  private TabbedDeckPanel deck = new TabbedDeckPanel(tabBar);

  /**
   * Creates an empty tab panel.
   */
  public MATTabPanel() {
    VerticalPanel panel = new VerticalPanel();
    panel.getElement().setId("panel_VerticalPanel");
    panel.add(tabBar);
    panel.add(deck);

    panel.setCellHeight(deck, "100%");
    tabBar.setWidth("100%");

    tabBar.addTabListener(this);
    initWidget(panel);
    setStyleName("gwt-TabPanel");
    deck.setStyleName("gwt-TabPanelBottom");
    // Add a11y role "tabpanel"
    Accessibility.setRole(deck.getElement(), Accessibility.ROLE_TABPANEL);
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
   */
  public void add(Widget w) {
    throw new UnsupportedOperationException(
        "A tabText parameter must be specified with add().");
  }

  /**
   * Adds a widget to the tab panel. If the Widget is already attached to the
   * TabPanel, it will be moved to the right-most index.
   * 
   * @param w the widget to be added
   * @param tabText the text to be shown on its tab
   */
  public void add(Widget w, String tabText, boolean isVisible) {
    insert(w, tabText, getWidgetCount(), isVisible);
  }

  /**
   * Adds a widget to the tab panel. If the Widget is already attached to the
   * TabPanel, it will be moved to the right-most index.
   * 
   * @param w the widget to be added
   * @param tabText the text to be shown on its tab
   * @param asHTML <code>true</code> to treat the specified text as HTML
   */
  public void add(Widget w, String tabText, boolean asHTML, boolean isVisible) {
    insert(w, tabText, asHTML, getWidgetCount(), isVisible);
  }

  /**
   * Adds a widget to the tab panel. If the Widget is already attached to the
   * TabPanel, it will be moved to the right-most index.
   * 
   * @param w the widget to be added
   * @param tabWidget the widget to be shown in the tab
   */
  public void add(Widget w, Widget tabWidget, boolean isVisible) {
    insert(w, tabWidget, getWidgetCount(), isVisible);
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
	 * Adds the tab listener.
	 * 
	 * @param listener
	 *            the listener
	 * @deprecated Use {@link #addBeforeSelectionHandler} and
	 *             {@link #addSelectionHandler} instead
	 */
  @Deprecated
  public void addTabListener(TabListener listener) {
    //ListenerWrapper.WrappedTabListener.add(this, listener);
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.HasWidgets#clear()
   */
  public void clear() {
    while (getWidgetCount() > 0) {
      remove(getWidget(0));
    }
  }

  /**
   * Gets the deck panel within this tab panel. Adding or removing Widgets from
   * the DeckPanel is not supported and will throw
   * UnsupportedOperationExceptions.
   * 
   * @return the deck panel
   */
  public DeckPanel getDeckPanel() {
    return deck;
  }

  /**
   * Gets the tab bar within this tab panel. Adding or removing tabs from from
   * the TabBar is not supported and will throw UnsupportedOperationExceptions.
   * 
   * @return the tab bar
   */
  public MATTabBar getTabBar() {
    return tabBar;
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidget(int)
   */
  public Widget getWidget(int index) {
    return deck.getWidget(index);
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetCount()
   */
  public int getWidgetCount() {
    return deck.getWidgetCount();
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetIndex(com.google.gwt.user.client.ui.Widget)
   */
  public int getWidgetIndex(Widget widget) {
    return deck.getWidgetIndex(widget);
  }

  /**
   * Inserts a widget into the tab panel. If the Widget is already attached to
   * the TabPanel, it will be moved to the requested index.
   * 
   * @param widget the widget to be inserted
   * @param tabText the text to be shown on its tab
   * @param asHTML <code>true</code> to treat the specified text as HTML
   * @param beforeIndex the index before which it will be inserted
   */
  public void insert(Widget widget, String tabText, boolean asHTML,
      int beforeIndex, boolean isVisible) {
    // Delegate updates to the TabBar to our DeckPanel implementation
    deck.insertProtected(widget, tabText, asHTML, beforeIndex, isVisible);
  }

  /**
   * Inserts a widget into the tab panel. If the Widget is already attached to
   * the TabPanel, it will be moved to the requested index.
   * 
   * @param widget the widget to be inserted.
   * @param tabWidget the widget to be shown on its tab.
   * @param beforeIndex the index before which it will be inserted.
   */
  public void insert(Widget widget, Widget tabWidget, int beforeIndex, boolean isVisible) {
    // Delegate updates to the TabBar to our DeckPanel implementation
    deck.insertProtected(widget, tabWidget, beforeIndex, isVisible);
  }

  /**
   * Inserts a widget into the tab panel. If the Widget is already attached to
   * the TabPanel, it will be moved to the requested index.
   * 
   * @param widget the widget to be inserted
   * @param tabText the text to be shown on its tab
   * @param beforeIndex the index before which it will be inserted
   */
  public void insert(Widget widget, String tabText, int beforeIndex, boolean isVisible) {
    insert(widget, tabText, false, beforeIndex, isVisible);
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.HasAnimation#isAnimationEnabled()
   */
  public boolean isAnimationEnabled() {
    return deck.isAnimationEnabled();
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
   */
  public Iterator<Widget> iterator() {
    // The Iterator returned by DeckPanel supports removal and will invoke
    // TabbedDeckPanel.remove(), which is an active function.
    return deck.iterator();
  }

  /**
	 * On before tab selected.
	 * 
	 * @param sender
	 *            the sender
	 * @param tabIndex
	 *            the tab index
	 * @return true, if successful
	 * @deprecated Use {@link BeforeSelectionHandler#onBeforeSelection} instead
	 */
  @Deprecated
  public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
    BeforeSelectionEvent<Integer> event = BeforeSelectionEvent.fire(this, tabIndex);
    return event == null || !event.isCanceled();
  }

  /**
	 * On tab selected.
	 * 
	 * @param sender
	 *            the sender
	 * @param tabIndex
	 *            the tab index
	 * @deprecated Use {@link SelectionHandler#onSelection} instead
	 */
  @Deprecated
  public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
    deck.showWidget(tabIndex);
    SelectionEvent.fire(this, tabIndex);
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.IndexedPanel#remove(int)
   */
  public boolean remove(int index) {
    // Delegate updates to the TabBar to our DeckPanel implementation
    return deck.remove(index);
  }

  /**
	 * Removes the given widget, and its associated tab.
	 * 
	 * @param widget
	 *            the widget to be removed
	 * @return true, if successful
	 */
  public boolean remove(Widget widget) {
    // Delegate updates to the TabBar to our DeckPanel implementation
    return deck.remove(widget);
  }

  /**
	 * Removes the tab listener.
	 * 
	 * @param listener
	 *            the listener
	 * @deprecated Use the {@link HandlerRegistration#removeHandler} method on
	 *             the object returned by and add*Handler method instead
	 */
  @Deprecated
  public void removeTabListener(TabListener listener) {
    //ListenerWrapper.WrappedTabListener.remove(this, listener);
  }

  /**
   * Programmatically selects the specified tab.
   * 
   * @param index the index of the tab to be selected
   */
  public void selectTab(int index) {
    tabBar.selectTab(index);
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.HasAnimation#setAnimationEnabled(boolean)
   */
  public void setAnimationEnabled(boolean enable) {
    deck.setAnimationEnabled(enable);
  }

  /**
   * Create a {@link SimplePanel} that will wrap the contents in a tab.
   * Subclasses can use this method to wrap tabs in decorator panels.
   * 
   * @return a {@link SimplePanel} to wrap the tab contents, or null to leave
   *         tabs unwrapped
   */
  protected SimplePanel createTabTextWrapper() {
    return null;
  }

  /**
	 * <b>Affected Elements:</b>
	 * <ul>
	 * <li>-bar = The tab bar.</li>
	 * <li>-bar-tab# = The element containing the content of the tab itself.</li>
	 * <li>-bar-tab-wrapper# = The cell containing the tab at the index.</li>
	 * <li>-bottom = The panel beneath the tab bar.</li>
	 * </ul>
	 * 
	 * @param baseID
	 *            the base id
	 * @see UIObject#onEnsureDebugId(String)
	 */
  @Override
  protected void onEnsureDebugId(String baseID) {
    super.onEnsureDebugId(baseID);
    tabBar.ensureDebugId(baseID + "-bar");
    deck.ensureDebugId(baseID + "-bottom");
  }
  
  /**
	 * implementation if Enableable interface consider setting Enablement of tab
	 * bar widgets.
	 * 
	 * @param enabled
	 *            the new enabled
	 */
  public void setEnabled(boolean enabled){
		for(int i = 0; i < tabBar.getTabCount(); i++){
			tabBar.setTabEnabled(i, enabled);
		}
  }
}

