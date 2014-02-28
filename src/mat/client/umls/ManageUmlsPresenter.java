package mat.client.umls;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.util.ClientConstants;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * The Class ManageUmlsPresenter.
 */
public class ManageUmlsPresenter implements MatPresenter{
	
	/**
	 * The Interface UMLSDisplay.
	 */
	public interface UMLSDisplay {
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		Widget asWidget();
		
		/**
		 * Gets the button bar.
		 * 
		 * @return the button bar
		 */
		SaveCancelButtonBar getButtonBar() ;
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the external link disclaimer.
		 * 
		 * @return the external link disclaimer
		 */
		VerticalPanel getExternalLinkDisclaimer();
		
		/**
		 * Gets the info message.
		 * 
		 * @return the info message
		 */
		HasHTML getInfoMessage();
		
		/**
		 * Gets the password.
		 * 
		 * @return the password
		 */
		HasValue<String> getPassword();
		
		/**
		 * Gets the password field.
		 * 
		 * @return the password field
		 */
		HasKeyDownHandlers getPasswordField();
		
		/**
		 * Gets the submit.
		 * 
		 * @return the submit
		 */
		HasClickHandlers getSubmit();
		
		/**
		 * Gets the umls external link.
		 * 
		 * @return the umls external link
		 */
		Anchor getUmlsExternalLink();
		
		/**
		 * Gets the umls trouble logging.
		 * 
		 * @return the umls trouble logging
		 */
		Anchor getUmlsTroubleLogging();
		
		/**
		 * Gets the userid.
		 * 
		 * @return the userid
		 */
		HasValue<String> getUserid();
		
		/**
		 * Gets the userid field.
		 * 
		 * @return the userid field
		 */
		HasKeyDownHandlers getUseridField();
		
		/**
		 * Sets the info message visible.
		 * 
		 * @param value
		 *            the new info message visible
		 */
		void setInfoMessageVisible(boolean value);
		
		/**
		 * Sets the initial focus.
		 */
		void setInitialFocus();
	}
	
	/** The display. */
	private  UMLSDisplay display;
	/** The panel. */
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();
	
	/**Key down handler to trap enter key.**/
	private KeyDownHandler submitOnEnterHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(final KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				submit();
			}
		}
	};
	
	/** The vsacapi service. */
	private VSACAPIServiceAsync vsacapiService  = MatContext.get().getVsacapiServiceAsync();
	/**Constructor.
	 *@param displayArg - {@link UMLSDisplay}.
	 * **/
	public ManageUmlsPresenter(final UMLSDisplay displayArg) {
		display = displayArg;
		resetWidget();
		display.getSubmit().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(final ClickEvent event) {
				submit();
			}
			
			
		});
		display.getUseridField().addKeyDownHandler(submitOnEnterHandler);
		display.getPasswordField().addKeyDownHandler(submitOnEnterHandler);
		display.getUmlsExternalLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				display.getExternalLinkDisclaimer().setVisible(true);
				display.getExternalLinkDisclaimer().getElement().removeAttribute("id");
				display.getExternalLinkDisclaimer().getElement().removeAttribute("role");
				display.getExternalLinkDisclaimer().getElement().setAttribute("id", "ExternalLinkDisclaimer");
				display.getExternalLinkDisclaimer().getElement().setAttribute("role", "alert");
			}
		});
		display.getUmlsTroubleLogging().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				display.getExternalLinkDisclaimer().setVisible(true);
				display.getExternalLinkDisclaimer().getElement().removeAttribute("id");
				display.getExternalLinkDisclaimer().getElement().removeAttribute("role");
				display.getExternalLinkDisclaimer().getElement().setAttribute("id", "ExternalLinkDisclaimer");
				display.getExternalLinkDisclaimer().getElement().setAttribute("role", "alert");
			}
		});
		display.getButtonBar().getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				display.getExternalLinkDisclaimer().setVisible(false);
				Window.open(ClientConstants.EXT_LINK_UMLS, "_blank", "");
			}
		});
		
		display.getButtonBar().getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				display.getExternalLinkDisclaimer().setVisible(false);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		resetWidget();
	}
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		String heading = "";
		panel.setHeading(heading, "UmlsLogin");
		FlowPanel fp = new FlowPanel();
		fp.getElement().setId("fp_FlowPanel");
		resetWidget();
		fp.add(display.asWidget());
		panel.setContent(fp);
		Mat.focusSkipLists("UmlsLogin");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return panel;
	}
	
	/**private method to invalidate UMLS's session by clearing UMLSSession Map for current HTTP session ID.**/
	private void invalidateVsacSession() {
		vsacapiService.inValidateVsacUser(new AsyncCallback<Void>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
			
			@Override
			public void onSuccess(final Void result) {
			}
		});
	}
	
	/**Method to clear all messages and un set user id and password input box's.**/
	private void resetWidget() {
		display.getErrorMessageDisplay().clear();
		display.setInfoMessageVisible(false);
		display.getExternalLinkDisclaimer().setVisible(false);
		display.getPassword().setValue("");
		display.getUserid().setValue("");
	}
	
	/** Private submit method - Calls to VSAC service.**/
	private void submit() {
		display.getErrorMessageDisplay().clear();
		display.setInfoMessageVisible(false);
		display.getExternalLinkDisclaimer().setVisible(false);
		if (display.getUserid().getValue().isEmpty()) {
			display.getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate().getLoginUserRequiredMessage());
		} else if (display.getPassword().getValue().isEmpty()) {
			display.getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate().getPasswordRequiredMessage());
		} else {
			vsacapiService.validateVsacUser(display.getUserid().getValue(),
					display.getPassword().getValue(), new AsyncCallback<Boolean>() {
				@Override
				public void onFailure(final Throwable caught) {
					display.getErrorMessageDisplay().setMessage(
							MatContext.get().getMessageDelegate().getUML_LOGIN_UNAVAILABLE());
					caught.printStackTrace();
					Mat.hideUMLSActive();
				}
				@Override
				public void onSuccess(final Boolean result) {
					if (result) {
						Mat.showUMLSActive();
						display.setInfoMessageVisible(true);
						display.getInfoMessage().setText(
								MatContext.get().getMessageDelegate().getUMLS_SUCCESSFULL_LOGIN());
						display.getUserid().setValue("");
						display.getPassword().setValue("");
						Mat.showUMLSActive();
						MatContext.get().restartUMLSSignout();
						MatContext.get().setUMLSLoggedIn(true);
					} else { //incorrect UMLS credential - no ticket is assigned.
						display.getErrorMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate().getUML_LOGIN_FAILED());
						Mat.hideUMLSActive();
						MatContext.get().setUMLSLoggedIn(false);
						invalidateVsacSession();
					}
				}
			});
		}
	}
	
}
