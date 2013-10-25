package mat.client.umls;

import mat.client.Mat;
import mat.client.MatPresenter;
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
		 * Gets the submit.
		 * 
		 * @return the submit
		 */
		HasClickHandlers getSubmit();
		
		/**
		 * Gets the userid.
		 * 
		 * @return the userid
		 */
		HasValue<String> getUserid();
		
		/**
		 * Gets the password.
		 * 
		 * @return the password
		 */
		HasValue<String> getPassword();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the info message.
		 * 
		 * @return the info message
		 */
		HasHTML getInfoMessage();

		/**
		 * Sets the info message visible.
		 * 
		 * @param value
		 *            the new info message visible
		 */
		void setInfoMessageVisible(boolean value);
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		Widget asWidget();
		
		/**
		 * Gets the userid field.
		 * 
		 * @return the userid field
		 */
		HasKeyDownHandlers getUseridField();
		
		/**
		 * Gets the password field.
		 * 
		 * @return the password field
		 */
		HasKeyDownHandlers getPasswordField();
		
		/**
		 * Sets the initial focus.
		 */
		void setInitialFocus();
		
		/**
		 * Gets the umls external link.
		 * 
		 * @return the umls external link
		 */
		Anchor getUmlsExternalLink();
		
		/**
		 * Gets the external link disclaimer.
		 * 
		 * @return the external link disclaimer
		 */
		VerticalPanel getExternalLinkDisclaimer();
		
		/**
		 * Gets the button bar.
		 * 
		 * @return the button bar
		 */
		SaveCancelButtonBar getButtonBar() ;
		
		/**
		 * Gets the umls trouble logging.
		 * 
		 * @return the umls trouble logging
		 */
		Anchor getUmlsTroubleLogging();
	}
	
	/** The vsacapi service. */
	private VSACAPIServiceAsync vsacapiService  = MatContext.get().getVsacapiServiceAsync();
	
	/** The display. */
	private  UMLSDisplay display;

	/**Constructor.
	 *@param displayArg - {@link UMLSDisplay}.
	 * **/
	public ManageUmlsPresenter(final UMLSDisplay displayArg) {
		this.display = displayArg;
		resetWidget();
		display.getSubmit().addClickHandler(new ClickHandler() {

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
	/**Key down handler to trap enter key.**/
	private KeyDownHandler submitOnEnterHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(final KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				submit();
			}
		}
	};

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
				@Override
				public void onFailure(final Throwable caught) {
					display.getErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate().getUML_LOGIN_UNAVAILABLE());
					caught.printStackTrace();
					Mat.hideUMLSActive();
				}
			});
		}
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

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		resetWidget();
		display.asWidget();
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		resetWidget();
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return display.asWidget();
	}

}
