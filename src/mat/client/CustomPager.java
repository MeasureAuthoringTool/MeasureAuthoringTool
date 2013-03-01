package mat.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;

/**
 * A pager for controlling a {@link HasRows} that only supports simple page
 * navigation.
 * 
 * <p>
 * <h3>Example</h3>
 * {@example com.google.gwt.examples.cellview.SimplePagerExample}
 * </p>
 */
public class CustomPager extends AbstractPager {

  /**
   * A ClientBundle that provides images for this widget.
   */
  public static interface Resources extends ClientBundle {

    /**
     * The image used to skip ahead multiple pages.
     */
    //@ImageOptions(flipRtl = true)
	  @Source("/images/simplePagerFastForward.png")
    ImageResource simplePagerFastForward();

    /**
     * The disabled "fast forward" image.
     */
    @ImageOptions(flipRtl = true)
	 @Source("/images/simplePagerFastForwardDisabled.png")
    ImageResource simplePagerFastForwardDisabled();

    /**
     * The image used to go to the first page.
     */
    @ImageOptions(flipRtl = true)
	@Source("/images/simplePagerFirstPage.png")
    ImageResource simplePagerFirstPage();

    /**
     * The disabled first page image.
     */
    @ImageOptions(flipRtl = true)
	@Source("/images/simplePagerFirstPageDisabled.png")
    ImageResource simplePagerFirstPageDisabled();

    /**
     * The image used to go to the last page.
     */
    @ImageOptions(flipRtl = true)
	@Source("/images/simplePagerLastPage.png")
    ImageResource simplePagerLastPage();

    /**
     * The disabled last page image.
     */
    @ImageOptions(flipRtl = true)
	@Source("/images/simplePagerLastPageDisabled.png")
    ImageResource simplePagerLastPageDisabled();

    /**
     * The image used to go to the next page.
     */
    @ImageOptions(flipRtl = true)
	@Source("/images/simplePagerNextPage.png")
    ImageResource simplePagerNextPage();

    /**
     * The disabled next page image.
     */
    @ImageOptions(flipRtl = true)
	@Source("/images/simplePagerNextPageDisabled.png")
    ImageResource simplePagerNextPageDisabled();

    /**
     * The image used to go to the previous page.
     */
    @ImageOptions(flipRtl = true)
	@Source("/images/simplePagerPreviousPage.png")
    ImageResource simplePagerPreviousPage();

    /**
     * The disabled previous page image.
     */
    @ImageOptions(flipRtl = true)
	@Source("/images/simplePagerPreviousPageDisabled.png")
    ImageResource simplePagerPreviousPageDisabled();

    /**
     * The styles used in this widget.
     */
    @Source("/images/SimplePager.css")
    Style simplePagerStyle();
  }

  /**
   * Styles used by this widget.
   */
  public static interface Style extends CssResource {

    /**
     * Applied to buttons.
     */
    String button();

    /**
     * Applied to disabled buttons.
     */
    String disabledButton();

    /**
     * Applied to the details text.
     */
    String pageDetails();
  }

  /**
   * The location of the text relative to the paging buttons.
   */
  public static enum TextLocation {
    CENTER, LEFT, RIGHT;
  }

  /**
   * An {@link Image} that acts as a button.
   */
  private static class ImageButton extends Image {
    private boolean disabled;
    private final ImageResource resDisabled;
    private final ImageResource resEnabled;
    private final String styleDisabled;

    public ImageButton(ImageResource resEnabled, ImageResource resDiabled,
        String disabledStyle) {
      super(resEnabled);
      this.resEnabled = resEnabled;
      this.resDisabled = resDiabled;
      this.styleDisabled = disabledStyle;
    }

    public boolean isDisabled() {
      return disabled;
    }

    @Override
    public void onBrowserEvent(Event event) {
      // Ignore events if disabled.
      if (disabled) {
        return;
      }
      super.onBrowserEvent(event);
    }

    public void setDisabled(boolean isDisabled) {
      if (this.disabled == isDisabled) {
        return;
      }

      this.disabled = isDisabled;
      if (disabled) {
        setResource(resDisabled);
        getElement().getParentElement().addClassName(styleDisabled);
      } else {
        setResource(resEnabled);
        getElement().getParentElement().removeClassName(styleDisabled);
      }
    }
  }

  private static int DEFAULT_FAST_FORWARD_ROWS = 1000;
  private static Resources DEFAULT_RESOURCES;

  private static Resources getDefaultResources() {
    if (DEFAULT_RESOURCES == null) {
      DEFAULT_RESOURCES = GWT.create(Resources.class);
    }
    return DEFAULT_RESOURCES;
  }

  private final ImageButton fastForward;

  private final int fastForwardRows;

  private final ImageButton firstPage;

  /**
   * We use an {@link HTML} so we can embed the loading image.
   */
  private final HTML label = new HTML();

  private final ImageButton lastPage;
  private final ImageButton nextPage;
  private final ImageButton prevPage;

  /**
   * The {@link Resources} used by this widget.
   */
  private final Resources resources;

  /**
   * The {@link Style} used by this widget.
   */
  private final Style style;

  /**
   * Construct a {@link CustomPager} with the default text location.
   */
  public CustomPager() {
    this(TextLocation.CENTER);
  }

  /**
   * Construct a {@link CustomPager} with the specified text location.
   * 
   * @param location the location of the text relative to the buttons
   */
  @UiConstructor
  // Hack for Google I/O demo
  public CustomPager(TextLocation location) {
    this(location, getDefaultResources(), true, DEFAULT_FAST_FORWARD_ROWS,
        false);
  }

  /**
   * Construct a {@link CustomPager} with the specified resources.
   * 
   * @param location the location of the text relative to the buttons
   * @param resources the {@link Resources} to use
   * @param showFastForwardButton if true, show a fast-forward button that
   *          advances by a larger increment than a single page
   * @param fastForwardRows the number of rows to jump when fast forwarding
   * @param showLastPageButton if true, show a button to go the the last page
   */
public CustomPager(TextLocation location, Resources resources,
      boolean showFastForwardButton, final int fastForwardRows,
      boolean showLastPageButton) {
    this.resources = resources;
    this.fastForwardRows = fastForwardRows;
    this.style = (Style) resources.simplePagerStyle();
    this.style.ensureInjected();

    // Create the buttons.
    String disabledStyle = style.disabledButton();
    final Anchor firstPageAnchor = new Anchor();
    firstPageAnchor.setTabIndex(0);
    firstPage = new ImageButton(resources.simplePagerFirstPage(),
        resources.simplePagerFirstPageDisabled(), disabledStyle);
    firstPage.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        firstPage();
      }
    });

    firstPageAnchor.addKeyDownHandler(new KeyDownHandler(){
	@Override
	public void onKeyDown(KeyDownEvent event) {
		if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !firstPage.isDisabled()) {
			firstPage();
		}
	}
   });
    firstPageAnchor.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(final ClickEvent event) {
			if(!firstPage.isDisabled())
				firstPage();
		}
	  }
	);
    firstPageAnchor.getElement().appendChild(firstPage.getElement());
    firstPageAnchor.setTitle("First Page");
    final Anchor nextPageAnchor = new Anchor();
    nextPageAnchor.setTabIndex(0);
    
    
    nextPage = new ImageButton(resources.simplePagerNextPage(),
        resources.simplePagerNextPageDisabled(), disabledStyle);
    nextPage.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        nextPage();
      }
    });
    nextPageAnchor.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(final ClickEvent event ) {
			if(!nextPage.isDisabled())
				nextPage();
		}
	  }
	);
    nextPageAnchor.addKeyDownHandler(new KeyDownHandler(){
    	@Override
    	public void onKeyDown(KeyDownEvent event) {
    		if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !nextPage.isDisabled()) {
    			nextPage();
    		}
    	}
       });
    
    nextPageAnchor.getElement().appendChild(nextPage.getElement());
    nextPageAnchor.setTitle("Next Page");
    final Anchor prevPageAnchor = new Anchor();
    prevPageAnchor.setTabIndex(0);
    prevPage = new ImageButton(resources.simplePagerPreviousPage(),
    								resources.simplePagerPreviousPageDisabled(), disabledStyle);
    prevPage.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        previousPage();
      }
    });
    prevPageAnchor.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(final ClickEvent event) {
			if(!prevPage.isDisabled())
				previousPage();
		}
	  }
	);
    prevPageAnchor.addKeyDownHandler(new KeyDownHandler(){
    	@Override
    	public void onKeyDown(KeyDownEvent event) {
    		if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !prevPage.isDisabled()) {
    			previousPage();
    		}
    	}
       });
    prevPageAnchor.setTitle("Previous Page");
    prevPageAnchor.getElement().appendChild(prevPage.getElement());
    
    final Anchor lastPageAnchor = new Anchor();
	lastPageAnchor.setTabIndex(0);
    if (showLastPageButton) {
    	lastPage = new ImageButton(resources.simplePagerLastPage(),
    			resources.simplePagerLastPageDisabled(), disabledStyle);
    	lastPage.addClickHandler(new ClickHandler() {
    		public void onClick(ClickEvent event) {
    			lastPage();
    		}
    	});
    	lastPageAnchor.addClickHandler(new ClickHandler() {
    		@Override
    		public void onClick(final ClickEvent event) {
    			if(!lastPage.isDisabled())
    				lastPage();
    		}
    	  }
    	);
    	lastPageAnchor.addKeyDownHandler(new KeyDownHandler(){
    		@Override
    		public void onKeyDown(KeyDownEvent event) {
    			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !lastPage.isDisabled()) {
    				lastPage();
    			}
    		}
    	});
    	lastPageAnchor.setTitle("Last Page");
    	lastPageAnchor.getElement().appendChild(lastPage.getElement());
    	
    } else {
    	lastPage = null;
    }
    final Anchor fastForwardAnchor = new Anchor();
	fastForwardAnchor.setTabIndex(0);
    if (showFastForwardButton) {
    	
    	fastForward = new ImageButton(resources.simplePagerFastForward(),
    			resources.simplePagerFastForwardDisabled(), disabledStyle);
    	fastForward.addClickHandler(new ClickHandler() {
    		public void onClick(ClickEvent event) {
    			setPage(getPage() + getFastForwardPages());
    		}
    	});
    	fastForwardAnchor.addClickHandler(new ClickHandler() {
    		@Override
    		public void onClick(final ClickEvent event) {
    			setPage(getPage() + getFastForwardPages());
    		}
    	  }
    	);
    	fastForwardAnchor.addKeyDownHandler(new KeyDownHandler(){
    		@Override
    		public void onKeyDown(KeyDownEvent event) {
    			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
    				setPage(getPage() + getFastForwardPages());
    			}
    		}
    	});

    	fastForwardAnchor.getElement().appendChild(fastForward.getElement());
    } else {
    	fastForward = null;
    }

    // Construct the widget.
    HorizontalPanel layout = new HorizontalPanel();
    layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    initWidget(layout);
    if (location == TextLocation.RIGHT) {
      layout.add(label);
    }
   // layout.add(firstPage);
    //layout.add(prevPage);
    layout.add(firstPageAnchor);
    layout.add(prevPageAnchor);
    if (location == TextLocation.CENTER) {
      layout.add(label);
    }
    //layout.add(nextPage);
    layout.add(nextPageAnchor);
    if (showFastForwardButton) {
      //layout.add(fastForward);
    	layout.add(fastForwardAnchor);
    }
    if (showLastPageButton) {
      //layout.add(lastPage);
    	layout.add(lastPageAnchor);
    }
    if (location == TextLocation.LEFT) {
      layout.add(label);
    }

    // Add style names to the cells.
    firstPage.getElement().getParentElement().addClassName(style.button());
    prevPage.getElement().getParentElement().addClassName(style.button());
    label.getElement().getParentElement().addClassName(style.pageDetails());
    nextPage.getElement().getParentElement().addClassName(style.button());
    if (showFastForwardButton) {
      fastForward.getElement().getParentElement().addClassName(style.button());
    }
    if (showLastPageButton) {
      lastPage.getElement().getParentElement().addClassName(style.button());
    }

    // Disable the buttons by default.
    setDisplay(null);
  }

  @Override
  public void firstPage() {
    super.firstPage();
  }

  @Override
  public int getPage() {
    return super.getPage();
  }

  @Override
  public int getPageCount() {
    return super.getPageCount();
  }

  @Override
  public boolean hasNextPage() {
    return super.hasNextPage();
  }

  @Override
  public boolean hasNextPages(int pages) {
    return super.hasNextPages(pages);
  }

  @Override
  public boolean hasPage(int index) {
    return super.hasPage(index);
  }

  @Override
  public boolean hasPreviousPage() {
    return super.hasPreviousPage();
  }

  @Override
  public boolean hasPreviousPages(int pages) {
    return super.hasPreviousPages(pages);
  }

  @Override
  public void lastPage() {
    super.lastPage();
  }

  @Override
  public void lastPageStart() {
    super.lastPageStart();
  }

  @Override
  public void nextPage() {
    super.nextPage();
  }

  @Override
  public void previousPage() {
    super.previousPage();
  }

  @Override
  public void setDisplay(HasRows display) {
    // Enable or disable all buttons.
    boolean disableButtons = (display == null);
    setFastForwardDisabled(disableButtons);
    setNextPageButtonsDisabled(disableButtons);
    setPrevPageButtonsDisabled(disableButtons);
    super.setDisplay(display);
  }

  @Override
  public void setPage(int index) {
    super.setPage(index);
  }

  @Override
  public void setPageSize(int pageSize) {
    super.setPageSize(pageSize);
  }

  @Override
  public void setPageStart(int index) {
    super.setPageStart(index);
  }

  /**
   * Let the page know that the table is loading. Call this method to clear all
   * data from the table and hide the current range when new data is being
   * loaded into the table.
   */
  public void startLoading() {
    getDisplay().setRowCount(0, true);
    label.setHTML("");
  }

  /**
   * Get the text to display in the pager that reflects the state of the pager.
   * 
   * @return the text
   */
  protected String createText() {
    // Default text is 1 based.
    NumberFormat formatter = NumberFormat.getFormat("#,###");
    HasRows display = getDisplay();
    Range range = display.getVisibleRange();
    int pageStart = range.getStart() + 1;
    int pageSize = range.getLength();
    int dataSize = display.getRowCount();
    int endIndex = Math.min(dataSize, pageStart + pageSize - 1);
    endIndex = Math.max(pageStart, endIndex);
    boolean exact = display.isRowCountExact();
    return formatter.format(pageStart) + "-" + formatter.format(endIndex)
        + (exact ? " of " : " of over ") + formatter.format(dataSize);
  }

  @Override
  protected void onRangeOrRowCountChanged() {
    HasRows display = getDisplay();
    label.setText(createText());

    // Update the prev and first buttons.
    setPrevPageButtonsDisabled(!hasPreviousPage());

    // Update the next and last buttons.
    if (isRangeLimited() || !display.isRowCountExact()) {
      setNextPageButtonsDisabled(!hasNextPage());
      setFastForwardDisabled(!hasNextPages(getFastForwardPages()));
    }
  }

  /**
   * Check if the next button is disabled. Visible for testing.
   */
  boolean isNextButtonDisabled() {
    return nextPage.isDisabled();
  }

  /**
   * Check if the previous button is disabled. Visible for testing.
   */
  boolean isPreviousButtonDisabled() {
    return prevPage.isDisabled();
  }

  /**
   * Get the number of pages to fast forward based on the current page size.
   * 
   * @return the number of pages to fast forward
   */
  private int getFastForwardPages() {
    int pageSize = getPageSize();
    return pageSize > 0 ? fastForwardRows / pageSize : 0;
  }

  /**
   * Enable or disable the fast forward button.
   * 
   * @param disabled true to disable, false to enable
   */
  private void setFastForwardDisabled(boolean disabled) {
    if (fastForward == null) {
      return;
    }
    if (disabled) {
      fastForward.setResource(resources.simplePagerFastForwardDisabled());
      fastForward.getElement().getParentElement().addClassName(
          style.disabledButton());
    } else {
      fastForward.setResource(resources.simplePagerFastForward());
      fastForward.getElement().getParentElement().removeClassName(
          style.disabledButton());
    }
  }

  /**
   * Enable or disable the next page buttons.
   * 
   * @param disabled true to disable, false to enable
   */
  private void setNextPageButtonsDisabled(boolean disabled) {
    nextPage.setDisabled(disabled);
    if (lastPage != null) {
      lastPage.setDisabled(disabled);
    }
  }

  /**
   * Enable or disable the previous page buttons.
   * 
   * @param disabled true to disable, false to enable
   */
  private void setPrevPageButtonsDisabled(boolean disabled) {
    firstPage.setDisabled(disabled);
    prevPage.setDisabled(disabled);
  }
}
