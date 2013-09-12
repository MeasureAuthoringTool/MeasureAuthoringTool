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

	public static interface UMLSDisplay {
		public HasClickHandlers getSubmit();
		public HasValue<String> getUserid();
		public HasValue<String> getPassword();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public HasHTML getInfoMessage();
		public void setWelcomeVisible(boolean value);
		public void setInfoMessageVisible(boolean value);
		public Widget asWidget();
		public HasKeyDownHandlers getUseridField();
		public HasKeyDownHandlers getPasswordField();
		public void setInitialFocus();
		public Anchor getUmlsExternalLink();
		public VerticalPanel getExternalLinkDisclaimer();
		public SaveCancelButtonBar getButtonBar() ;
		public Anchor getUmlsTroubleLogging();
	}
	
	VSACAPIServiceAsync vsacapiService  = MatContext.get().getVsacapiServiceAsync();
	private  UMLSDisplay display;
	
	
	public ManageUmlsPresenter(UMLSDisplay displayArg) {
		this.display = displayArg;
		resetWidget();
		display.getSubmit().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				submit();
			}


		});
		
		display.getUseridField().addKeyDownHandler(submitOnEnterHandler);
		display.getPasswordField().addKeyDownHandler(submitOnEnterHandler);
		display.getUmlsExternalLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				display.getExternalLinkDisclaimer().setVisible(true);
			}
		});
		
		display.getUmlsTroubleLogging().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				display.getExternalLinkDisclaimer().setVisible(true);
			}
		});
		
		display.getButtonBar().getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				display.getExternalLinkDisclaimer().setVisible(false);
				Window.open(ClientConstants.EXT_LINK_UMLS, "_blank", "");
			}
		});
		
		display.getButtonBar().getCancelButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
			
				display.getExternalLinkDisclaimer().setVisible(false);
			}
		});
		
	}
	
	private KeyDownHandler submitOnEnterHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				submit();
			}
		}
	};
	
	private void submit() {
		display.getErrorMessageDisplay().clear();
		display.setInfoMessageVisible(false);
		display.getExternalLinkDisclaimer().setVisible(false);
		if(display.getUserid().getValue().isEmpty()) {
			display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getLoginUserRequiredMessage());
		}else if(display.getPassword().getValue().isEmpty()) {
			display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getPasswordRequiredMessage());
		}else{
			vsacapiService.validateVsacUser(display.getUserid().getValue(), display.getPassword().getValue(), new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
				
					if(result!=null){
						Mat.showUMLSActive();
						display.setInfoMessageVisible(true);
						display.getInfoMessage().setText(MatContext.get().getMessageDelegate().getUMLS_SUCCESSFULL_LOGIN());
						Window.alert("Ticket Granted for session :  " +result);
						display.getUserid().setValue("");
						display.getPassword().setValue("");
						Mat.showUMLSActive();
						MatContext.get().restartUMLSSignout();
					}
					else{//incorrect UMLS credential - no ticket is assigned.
						display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getUML_LOGIN_FAILED());
						Mat.hideUMLSActive();
					}
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getUML_LOGIN_UNAVAILABLE());
					caught.printStackTrace();
					Mat.hideUMLSActive();
				}
			});
		}
	}
	
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
