package mat.client.audit;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import mat.client.shared.LabelBuilder;

import java.util.Date;

/**
 * The Class AuditLogWidget.
 */
public class AuditLogWidget extends Composite {

	/**
	 * Instantiates a new audit log widget.
	 * 
	 * @param activityType
	 *            the activity type
	 * @param userId
	 *            the user id
	 * @param timeStamp
	 *            the time stamp
	 * @param additionalInfo
	 *            the additional info
	 */
	public AuditLogWidget(String activityType, String userId, Date timeStamp, String additionalInfo) {
		VerticalPanel auditLogPanel = new VerticalPanel();
		auditLogPanel.setWidth("100%");
		auditLogPanel.getElement().setId("auditLogPanel_VerticalPanel");
		HorizontalPanel eventPanel = new HorizontalPanel();
		eventPanel.getElement().setId("eventPanel_HorizontalPanel");

		Label activityTypeLbl = new Label(activityType);
		activityTypeLbl.setStylePrimaryName("noBorder");
		eventPanel.add(activityTypeLbl);

		Label userIdLbl = new Label(userId);
		userIdLbl.setStylePrimaryName("noBorder");
		eventPanel.add(userIdLbl);

		DateTimeFormat formatter = DateTimeFormat.getFormat("MM'/'dd'/'yyyy h:mm:ss a");
		String dString = formatter.format(timeStamp);
		dString = dString + " CST";
		Label timeStampLbl = new Label(dString);
		timeStampLbl.setStylePrimaryName("noBorder");
		eventPanel.add(timeStampLbl);

		eventPanel.setStylePrimaryName("noBorder");
		eventPanel.setSize("550px", "30px");  //Reducing the height of the event if there is no additionalInfo
		auditLogPanel.add(eventPanel);

		HorizontalPanel additionalInfoPanel = new HorizontalPanel();
		additionalInfoPanel.setWidth("100%");
		additionalInfoPanel.getElement().setId("additionalInfoPanel_HorizontalPanel");

		if (additionalInfo != null) {
			TextArea additionalInfoTxtArea = new TextArea();
			additionalInfoTxtArea.getElement().setId("additionalInfoTxtArea_TextArea");
			additionalInfoTxtArea.setReadOnly(true);
			additionalInfoTxtArea.setWidth("90%");
			additionalInfoTxtArea.setHeight("80px");
			additionalInfoTxtArea.setValue(additionalInfo);
			additionalInfoPanel.add(additionalInfoTxtArea);
			additionalInfoPanel.setSize("550px", "100px");
			additionalInfoPanel.setStylePrimaryName("noBorder");
			auditLogPanel.add(LabelBuilder.buildInvisibleLabel(new Label(), "Event Information"));
			auditLogPanel.add(additionalInfoPanel);
			auditLogPanel.setStylePrimaryName("noBorder");
		}
		initWidget(auditLogPanel);
	}
}
