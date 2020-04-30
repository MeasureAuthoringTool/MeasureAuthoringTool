package mat.client.harp;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTML;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.html.Div;

public class NewUserRegistrationModal extends Modal {

    private Button closeButton = new Button("Close");


    private String bodyHtml = "Please submit a ticket via <a target=\"_blank\" href=\"https://oncprojectracking.healthit.gov/support/login.jsp\">the Office of the National Coordinator (ONC) </a> Jira System and follow the directions on the page " +
            "to create a new MAT account. <p style=\"font-size: small; padding-top: 18px;\"> Note: Please include your organization's OID in the Description field.</p>";

    public NewUserRegistrationModal() {
        setHeight("442px");
        setWidth("600px");

        ModalHeader modalHeader = new ModalHeader();
        Div headerDiv = new Div();
        headerDiv.add(new HTML("New MAT User"));
        headerDiv.getElement().getStyle().setProperty("fontSize", "small");
        modalHeader.getElement().getStyle().setColor("white");
        modalHeader.getElement().getStyle().setBackgroundColor("#0E81C5");
        modalHeader.add(headerDiv);
        modalHeader.setClosable(false);

        ModalBody modalBody = new ModalBody();

        modalBody.getElement().getStyle().setMarginRight(10, Style.Unit.PX);
        modalBody.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);

        modalBody.add(new HTML(bodyHtml));

        ModalFooter modalFooter = new ModalFooter();
        closeButton.getElement().getStyle().setProperty("fontSize", "small");
        modalFooter.add(closeButton);

        add(modalHeader);
        add(modalBody);
        add(modalFooter);

        getElement().focus();
    }

    public Button getCloseButton() {
        return closeButton;
    }

}
