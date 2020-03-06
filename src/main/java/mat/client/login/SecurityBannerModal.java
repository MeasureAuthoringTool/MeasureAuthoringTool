package mat.client.login;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTML;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.html.Span;

public class SecurityBannerModal extends Modal {
    private Button acceptButton = new Button("I Accept");
    private Button declineButton = new Button("Decline");
    private String bodyHtml = "<br/><p>This warning banner provides privacy and security notices consistent with " +
            "applicable federal laws, directives, and other federal guidance for accessing this Government system, " +
            "which includes (1) this computer network, (2) all computers connected to this network, and (3) all " +
            "devices and storage media attached to this network or to a computer on this network.</p><br/>" +
            "<p>This system is provided for Government authorized use only.</p><br/>" +
            "<p>Unauthorized or improper use of this system is prohibited and may result in disciplinary action " +
            "and/or civil and criminal penalties</p><br/>" +
            "<p>Personal use of social media and networking sites on this system is limited as to not interfere with " +
            "official work duties and is subject to monitoring.</p><br/>" +
            "<p>By using this system, you understand and consent to the following:</p><br/>" +
            "<ul>" +
            "   <li>The Government may monitor, record, and audit your system usage, including usage of personal " +
            "devices and email systems for official duties or to conduct HHS business. Therefore, you have no " +
            "reasonable expectation of privacy regarding any communication or data transiting or stored on this " +
            "system. At any time, and for any lawful Government purpose, the government may monitor, intercept, and " +
            "search and seize any communication or data transiting or stored on this system.</li><br/>" +
            "   <li>Any communication or data transiting or stored on this system must be disclosed or used for any " +
            "lawful Government purpose.</li>" +
            "</ul><br/>" +
            "<p style=\"color: #337AB7\">To continue, you must accept the terms and conditions. If you decline, your " +
            "login will automatically be cancelled.</p>";

    private String paperworkReductionActHtml = "<p>According to the Paperwork Reduction Act of 1995, no persons are " +
            "required to respond to a collection of information unless it displays a valid OMB control number. The " +
            "valid OMB control number for this information collection is 0938-1236. The time required to complete " +
            "this information collection is estimated to average 20 minutes per response, including the time to " +
            "review instructions, search existing data resources, gather the data needed, and complete and review " +
            "the information collection.</p><br/>" +
            "<p>If you have comments concerning the accuracy of the time estimate(s) or " +
            "suggestions for improving this form, please write to: <br/><br/>" +
            "CMS<br/>" +
            "7500 Security Boulevard<br/>" +
            "Attn: PRA Reports Clearance Officer, Mail Stop C4-26-05<br />" +
            "Baltimore, Maryland 21244-1850" +
            "</p>";

    private Modal paperworkReductionActPopup;

    public SecurityBannerModal() {
        builldPaperworkReductionActPopup();
        setHeight("800px");
        setWidth("1000px");

        ModalHeader modalHeader = new ModalHeader();
        modalHeader.setTitle("System Use Notification");
        modalHeader.getElement().getStyle().setColor("white");
        modalHeader.getElement().getStyle().setBackgroundColor("blue");
        modalHeader.setClosable(false);

        ModalBody modalBody = new ModalBody();
        FlowPanel headerPanel = new FlowPanel();

        Anchor paperworkAnchor = new Anchor();
        paperworkAnchor.setText("Paperwork Reduction Act");
        paperworkAnchor.addClickHandler(e -> {
            this.hide();
            paperworkReductionActPopup.show();
        });

        headerPanel.add(new Span("OMB No 0938-1236 | Expiration Date: 04/30/2017 (OMB Re-Certification Pending) | "));
        headerPanel.add(paperworkAnchor);
        modalBody.getElement().getStyle().setMarginRight(10, Style.Unit.PX);
        modalBody.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);

        modalBody.add(headerPanel);
        modalBody.add(new HTML(bodyHtml));

        ModalFooter modalFooter = new ModalFooter();
        modalFooter.add(acceptButton);
        modalFooter.add(declineButton);

        add(modalHeader);
        add(modalBody);
        add(modalFooter);

        getElement().focus();
    }

    private void builldPaperworkReductionActPopup() {
        paperworkReductionActPopup = new Modal();
        paperworkReductionActPopup.setHeight("400px");
        paperworkReductionActPopup.setWidth("1000px");
        paperworkReductionActPopup.addHideHandler(e -> this.show());

        ModalHeader modalHeader = new ModalHeader();
        modalHeader.setTitle("Paperwork Reduction Act");
        modalHeader.getElement().getStyle().setColor("white");
        modalHeader.getElement().getStyle().setBackgroundColor("blue");
        modalHeader.setClosable(true);


        ModalBody modalBody = new ModalBody();
        modalBody.getElement().getStyle().setMarginRight(10, Style.Unit.PX);
        modalBody.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        modalBody.add(new HTML(paperworkReductionActHtml));

        ModalFooter modalFooter = new ModalFooter();
        Button ok = new Button("OK", e -> paperworkReductionActPopup.hide());
        modalFooter.add(ok);
        paperworkReductionActPopup.add(modalHeader);
        paperworkReductionActPopup.add(modalBody);
        paperworkReductionActPopup.add(modalFooter);

        paperworkReductionActPopup.getElement().focus();
    }

    public Button getAcceptButton() {
        return acceptButton;
    }

    public Button getDeclineButton() {
        return declineButton;
    }
}
