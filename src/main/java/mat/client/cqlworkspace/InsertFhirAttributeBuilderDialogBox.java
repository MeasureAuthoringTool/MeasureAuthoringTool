package mat.client.cqlworkspace;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.DOM;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.cqlworkspace.attributes.FhirAttribute;
import mat.client.cqlworkspace.attributes.FhirDataType;
import mat.client.cqlworkspace.attributes.InsertFhirAttributesDialogModel;
import mat.client.cqlworkspace.attributes.InsertFhirAttributesDialogDisplay;
import mat.client.cqlworkspace.attributes.InsertFhirAttributesDialogView;

public class InsertFhirAttributeBuilderDialogBox {

    private static final List<FhirDataType> DATA = Arrays.asList(
            new FhirDataType(DOM.createUniqueId(), "Adverse Event",
                    Arrays.asList(
                            new FhirAttribute(DOM.createUniqueId(), "code"),
                            new FhirAttribute(DOM.createUniqueId(), "type"),
                            new FhirAttribute(DOM.createUniqueId(), "severity"),
                            new FhirAttribute(DOM.createUniqueId(), "facilityLocaion"),
                            new FhirAttribute(DOM.createUniqueId(), "id"),
                            new FhirAttribute(DOM.createUniqueId(), "recorder"),
                            new FhirAttribute(DOM.createUniqueId(), "authorDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "relevantDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "patientId")
                    )
            ),
            new FhirDataType(DOM.createUniqueId(), "Allergy/Intolerance",
                    Arrays.asList(
                            new FhirAttribute(DOM.createUniqueId(), "code"),
                            new FhirAttribute(DOM.createUniqueId(), "prevalencePeriod"),
                            new FhirAttribute(DOM.createUniqueId(), "type"),
                            new FhirAttribute(DOM.createUniqueId(), "severity"),
                            new FhirAttribute(DOM.createUniqueId(), "id"),
                            new FhirAttribute(DOM.createUniqueId(), "recorder"),
                            new FhirAttribute(DOM.createUniqueId(), "authorDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "relevantDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "patientId")
                    )),
            new FhirDataType(DOM.createUniqueId(), "Assessment, Not Ordered",
                    Arrays.asList(
                            new FhirAttribute(DOM.createUniqueId(), "patientId"),
                            new FhirAttribute(DOM.createUniqueId(), "authorDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "negationRationale"),
                            new FhirAttribute(DOM.createUniqueId(), "code"),
                            new FhirAttribute(DOM.createUniqueId(), "id"),
                            new FhirAttribute(DOM.createUniqueId(), "patientId"),
                            new FhirAttribute(DOM.createUniqueId(), "code")
                    )),
            new FhirDataType(DOM.createUniqueId(), "Assessment, Not Performed",
                    Arrays.asList(
                            new FhirAttribute(DOM.createUniqueId(), "patientId"),
                            new FhirAttribute(DOM.createUniqueId(), "authorDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "negationRationale"),
                            new FhirAttribute(DOM.createUniqueId(), "code"),
                            new FhirAttribute(DOM.createUniqueId(), "id"),
                            new FhirAttribute(DOM.createUniqueId(), "patientId"),
                            new FhirAttribute(DOM.createUniqueId(), "code")
                    )),
            new FhirDataType(DOM.createUniqueId(), "Assessment, Not Recommended",
                    Arrays.asList(
                            new FhirAttribute(DOM.createUniqueId(), "patientId"),
                            new FhirAttribute(DOM.createUniqueId(), "authorDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "negationRationale"),
                            new FhirAttribute(DOM.createUniqueId(), "code"),
                            new FhirAttribute(DOM.createUniqueId(), "id"),
                            new FhirAttribute(DOM.createUniqueId(), "patientId"),
                            new FhirAttribute(DOM.createUniqueId(), "code")
                    )),
            new FhirDataType(DOM.createUniqueId(), "Assessment, Order",
                    Arrays.asList(
                            new FhirAttribute(DOM.createUniqueId(), "patientId"),
                            new FhirAttribute(DOM.createUniqueId(), "reason"),
                            new FhirAttribute(DOM.createUniqueId(), "code"),
                            new FhirAttribute(DOM.createUniqueId(), "id"),
                            new FhirAttribute(DOM.createUniqueId(), "authorDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "requester"),
                            new FhirAttribute(DOM.createUniqueId(), "patientId")
                    )),
            new FhirDataType(DOM.createUniqueId(), "Assessment, Performed",
                    Arrays.asList(
                            new FhirAttribute(DOM.createUniqueId(), "patientId"),
                            new FhirAttribute(DOM.createUniqueId(), "code"),
                            new FhirAttribute(DOM.createUniqueId(), "method"),
                            new FhirAttribute(DOM.createUniqueId(), "components"),
                            new FhirAttribute(DOM.createUniqueId(), "relevantDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "authorDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "relevantPeriod"),
                            new FhirAttribute(DOM.createUniqueId(), "result"),
                            new FhirAttribute(DOM.createUniqueId(), "reason"),
                            new FhirAttribute(DOM.createUniqueId(), "relatedTo"),
                            new FhirAttribute(DOM.createUniqueId(), "performer"),
                            new FhirAttribute(DOM.createUniqueId(), "id")
                    )),
            new FhirDataType(DOM.createUniqueId(), "Assessment, Recommended",
                    Arrays.asList(
                            new FhirAttribute(DOM.createUniqueId(), "patientId"),
                            new FhirAttribute(DOM.createUniqueId(), "code"),
                            new FhirAttribute(DOM.createUniqueId(), "reason"),
                            new FhirAttribute(DOM.createUniqueId(), "authorDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "requester"),
                            new FhirAttribute(DOM.createUniqueId(), "id")
                    )),
            new FhirDataType(DOM.createUniqueId(), "Care Goal",
                    Arrays.asList(
                            new FhirAttribute(DOM.createUniqueId(), "patientId"),
                            new FhirAttribute(DOM.createUniqueId(), "targetOutcome"),
                            new FhirAttribute(DOM.createUniqueId(), "code"),
                            new FhirAttribute(DOM.createUniqueId(), "relevantPeriod"),
                            new FhirAttribute(DOM.createUniqueId(), "statusDate"),
                            new FhirAttribute(DOM.createUniqueId(), "relatedTo"),
                            new FhirAttribute(DOM.createUniqueId(), "performer"),
                            new FhirAttribute(DOM.createUniqueId(), "id")
                    )),
            new FhirDataType(DOM.createUniqueId(), "Communication, Not Performed",
                    Arrays.asList(
                            new FhirAttribute(DOM.createUniqueId(), "patientId"),
                            new FhirAttribute(DOM.createUniqueId(), "authorDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "category"),
                            new FhirAttribute(DOM.createUniqueId(), "negationRationale"),
                            new FhirAttribute(DOM.createUniqueId(), "code"),
                            new FhirAttribute(DOM.createUniqueId(), "sender"),
                            new FhirAttribute(DOM.createUniqueId(), "recipient"),
                            new FhirAttribute(DOM.createUniqueId(), "id")
                    )),
            new FhirDataType(DOM.createUniqueId(), "Communication, Performed",
                    Arrays.asList(
                            new FhirAttribute(DOM.createUniqueId(), "patientId"),
                            new FhirAttribute(DOM.createUniqueId(), "category"),
                            new FhirAttribute(DOM.createUniqueId(), "medium"),
                            new FhirAttribute(DOM.createUniqueId(), "code"),
                            new FhirAttribute(DOM.createUniqueId(), "sentDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "receivedDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "id"),
                            new FhirAttribute(DOM.createUniqueId(), "authorDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "sender"),
                            new FhirAttribute(DOM.createUniqueId(), "recipient"),
                            new FhirAttribute(DOM.createUniqueId(), "basedOn")
                    )),
            new FhirDataType(DOM.createUniqueId(), "Device, Applied",
                    Arrays.asList(
                            new FhirAttribute(DOM.createUniqueId(), "anatomicalLocationSite"),
                            new FhirAttribute(DOM.createUniqueId(), "code"),
                            new FhirAttribute(DOM.createUniqueId(), "reason"),
                            new FhirAttribute(DOM.createUniqueId(), "relevantPeriod"),
                            new FhirAttribute(DOM.createUniqueId(), "sentDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "id"),
                            new FhirAttribute(DOM.createUniqueId(), "relevantDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "performer"),
                            new FhirAttribute(DOM.createUniqueId(), "patientId")
                    )),
            new FhirDataType(DOM.createUniqueId(), "Device, Not Applied",
                    Arrays.asList(
                            new FhirAttribute(DOM.createUniqueId(), "anatomicalLocationSite"),
                            new FhirAttribute(DOM.createUniqueId(), "code"),
                            new FhirAttribute(DOM.createUniqueId(), "reason"),
                            new FhirAttribute(DOM.createUniqueId(), "relevantPeriod"),
                            new FhirAttribute(DOM.createUniqueId(), "sentDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "id"),
                            new FhirAttribute(DOM.createUniqueId(), "relevantDatetime"),
                            new FhirAttribute(DOM.createUniqueId(), "performer"),
                            new FhirAttribute(DOM.createUniqueId(), "patientId")
                    ))
    );

    public static void showAttributesDialogBox(final AceEditor editor) {
        InsertFhirAttributesDialogDisplay dialogDisplay = new InsertFhirAttributesDialogView(editor, new InsertFhirAttributesDialogModel(DATA));
        dialogDisplay.show();
    }

}
