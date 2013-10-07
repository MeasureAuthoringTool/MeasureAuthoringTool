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


public class ManageUmlsPresenter implements MatPresenter{

	public interface UMLSDisplay {
		HasClickHandlers getSubmit();
		HasValue<String> getUserid();
		HasValue<String> getPassword();
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		HasHTML getInfoMessage();

		void setInfoMessageVisible(boolean value);
		Widget asWidget();
		HasKeyDownHandlers getUseridField();
		HasKeyDownHandlers getPasswordField();
		void setInitialFocus();
		Anchor getUmlsExternalLink();
		VerticalPanel getExternalLinkDisclaimer();
		SaveCancelButtonBar getButtonBar() ;
		Anchor getUmlsTroubleLogging();
	}
	private VSACAPIServiceAsync vsacapiService  = MatContext.get().getVsacapiServiceAsync();
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

	@Override
	public void beforeDisplay() {
		resetWidget();
		display.asWidget();
	}

	@Override
	public void beforeClosingDisplay() {
		resetWidget();
	}

	@Override
	public Widget getWidget() {
		return display.asWidget();
	}

}
