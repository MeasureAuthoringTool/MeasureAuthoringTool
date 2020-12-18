package mat.client.cqlworkspace.valuesets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.ConstantMessages;
import org.gwtbootstrap3.client.ui.ListBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CQLAppliedValueSetUtility {
    private static final Logger log = Logger.getLogger(CQLAppliedValueSetUtility.class.getSimpleName());

    public String getExpansionProfileValue(ListBox inputListBox) {
        return (inputListBox.getSelectedIndex() >= 0) ? inputListBox.getValue(inputListBox.getSelectedIndex()) : "";
    }

    public String getDataTypeText(ListBoxMVP inputListBox) {
        return (inputListBox.getSelectedIndex() >= 0) ? inputListBox.getItemText(inputListBox.getSelectedIndex()) : "";
    }

    public String getDataTypeValue(ListBoxMVP inputListBox) {
        return (inputListBox.getSelectedIndex() >= 0) ? inputListBox.getValue(inputListBox.getSelectedIndex()) : "";
    }

    public static boolean checkForEnable() {
        return MatContext.get().getMeasureLockService().checkForEditPermission();
    }

    public static String getProgramTitle(CQLQualityDataSetDTO object, String program) {
        String title = null;
        if (!object.getOid().equals(ConstantMessages.USER_DEFINED_QDM_OID)) {
            title = "Program : " + program;
        }
        return title;
    }

    public static String getProgramColumnProgram(CQLQualityDataSetDTO object) {
        String program = null;
        if (!object.getOid().equals(ConstantMessages.USER_DEFINED_QDM_OID)) {
            program = (object.getProgram() == null ? "" : object.getProgram());
        } else {
            program = "";
        }
        return program;
    }

    public static String buildNameValue(CQLQualityDataSetDTO object) {
        StringBuilder value = new StringBuilder();
        StringBuilder qdmType = new StringBuilder();
        if (!object.getOid().equals(ConstantMessages.USER_DEFINED_QDM_OID) &&
                object.getValueSetType() != null) {
            qdmType.append(" (").append(object.getValueSetType().substring(0, 1)).append(")");
        }
        value.append(object.getName()).append(qdmType.toString());
        return value.toString();
    }

    public static StringBuilder buildOIDTitle(CQLQualityDataSetDTO object) {
        StringBuilder title = new StringBuilder();
        title.append("OID : ");
        if (object.getOid().equals(ConstantMessages.USER_DEFINED_QDM_OID)) {
            title.append(ConstantMessages.USER_DEFINED_QDM_NAME);
        } else {
            title.append(object.getOid());
        }
        return title;
    }

    public static String buildOidColumnOid(CQLQualityDataSetDTO object) {
        String oid = null;
        if (object.getOid().equals(ConstantMessages.USER_DEFINED_QDM_OID)) {
            oid = ConstantMessages.USER_DEFINED_CONTEXT_DESC;
        } else {
            oid = object.getOid();
        }
        return oid;
    }

    public static String buildNameTitle(String value) {
        StringBuilder title = new StringBuilder();
        title.append("Name : ").append(value);
        title.append("");
        return title.toString();
    }

    public static String buildReleaseTitle(CQLQualityDataSetDTO object) {
        StringBuilder title = new StringBuilder();
        if (!object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
            title.append("Release : ").append(object.getRelease());
        }
        return title.toString();
    }

    public static String buildReleaseColumnRelease(CQLQualityDataSetDTO object) {
        String release = "";
        if (!object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
            release = object.getRelease() != null ? object.getRelease() : "";
        }
        return release;
    }

    public static void initializeReleaseListBoxContent(ListBox releaseBox) {
        releaseBox.clear();
        releaseBox.setEnabled(true);
        releaseBox.addItem(MatContext.PLEASE_SELECT, MatContext.PLEASE_SELECT);
    }

    private static void setProgram(String program, CQLAppliedValueSetView view) {
        for (int i = 0; i < view.getProgramListBox().getItemCount(); i++) {
            if (program.equals(view.getProgramListBox().getItemText(i))) {
                view.getProgramListBox().setSelectedIndex(i);
                break;
            }
        }
    }

    private static void setRelease(String release, CQLAppliedValueSetView view) {
        for (int i = 0; i < view.getReleaseListBox().getItemCount(); i++) {
            if (release.equals(view.getReleaseListBox().getItemText(i))) {
                view.getReleaseListBox().setSelectedIndex(i);
                break;
            }
        }
    }

    public static void setProgramsAndReleases(String program, String release, CQLAppliedValueSetView view) {
        if (program != null) {
            setProgram(program, view);
            loadReleases(view.getReleaseListBox(), view.getProgramListBox());
            if (release != null) {
                setRelease(release, view);
            }
        }
        view.setProgramReleaseBoxEnabled(true);
    }

    public static void loadPrograms(ListBox programBox) {
        log.log(Level.INFO, "Entering loadPrograms");
        programBox.clear();
        programBox.addItem(MatContext.PLEASE_SELECT, MatContext.PLEASE_SELECT);
        HashMap<String, List<String>> pgmRelMap = (HashMap<String, List<String>>) MatContext.get().getProgramToReleases();
        pgmRelMap.forEach((k, v) -> programBox.addItem(k));
        log.log(Level.INFO, "Exiting loadPrograms");
    }

    //loadPrograms() MUST be called before loadReleases()
    public static void loadReleases(ListBox releaseBox, ListBox programBox) {
        log.log(Level.INFO, "Entering loadReleases");
        releaseBox.clear();
        releaseBox.setEnabled(true);
        List<String> releases = new ArrayList<>();
        releaseBox.addItem(MatContext.PLEASE_SELECT, MatContext.PLEASE_SELECT);
        String program = programBox.getSelectedValue();
        log.log(Level.INFO, "Program: " + program);
        if (!program.equals(MatContext.PLEASE_SELECT)) {
            releases.addAll(MatContext.get().getProgramToReleases().get(programBox.getSelectedValue()));
            for (String release : releases) {
                releaseBox.addItem(release, release);
            }
        }
        log.log(Level.INFO, "Leaving loadReleases");
    }


    public static void loadProgramsAndReleases(ListBox programs, ListBox releases) {
        loadPrograms(programs);
        loadReleases(releases, programs);
    }

    public static SafeHtml getOIDColumnToolTip(String columnText,
                                               StringBuilder title, boolean hasImage, boolean isUserDefined) {
        if (hasImage && !isUserDefined) {
            String htmlConstant = "<html>"
                    + "<head> </head> <body><img src =\"images/bullet_tick.png\" alt=\"Value set is updated from VSAC.\""
                    + "title = \"Value set is updated from VSAC.\"/>"
                    + "<span title='" + title + "'>"
                    + columnText + "</span></body>" + "</html>";
            return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant)
                    .toSafeHtml();
        } else if (hasImage && isUserDefined) {
            String htmlConstant = "<html>"
                    + "<head> </head> <body><img src =\"images/userDefinedWarning.png\""
                    + "alt=\"Warning : Value set is not available in VSAC.\""
                    + " title=\"Value set is not available in VSAC.\"/>"
                    + "<span title='" + title + "'>"
                    + columnText + "</span></body>" + "</html>";
            return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant)
                    .toSafeHtml();
        } else {
            String htmlConstant = "<html>"
                    + "<head> </head> <body><span title='"
                    + title + "'>" + columnText + "</span></body>" + "</html>";
            return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant)
                    .toSafeHtml();
        }
    }


    public static CompositeCell<CQLQualityDataSetDTO> getCompositeCell(final boolean isEditable, HasCell<CQLQualityDataSetDTO, ?> cellToAdd) {
        final List<HasCell<CQLQualityDataSetDTO, ?>> cells = new LinkedList<>();

        if (isEditable) {
            cells.add(cellToAdd);
        }

        CompositeCell<CQLQualityDataSetDTO> cell = new CompositeCell<CQLQualityDataSetDTO>(
                cells) {
            @Override
            public void render(Context context, CQLQualityDataSetDTO object,
                               SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<table tabindex=\"-1\"><tbody><tr tabindex=\"-1\">");
                for (HasCell<CQLQualityDataSetDTO, ?> hasCell : cells) {
                    render(context, object, sb, hasCell);
                }
                sb.appendHtmlConstant("</tr></tbody></table>");
            }

            @Override
            protected <X> void render(Context context,
                                      CQLQualityDataSetDTO object, SafeHtmlBuilder sb,
                                      HasCell<CQLQualityDataSetDTO, X> hasCell) {
                Cell<X> cell = hasCell.getCell();
                sb.appendHtmlConstant("<td class='emptySpaces' tabindex=\"-1\">");
                if ((object != null)) {
                    cell.render(context, hasCell.getValue(object), sb);
                } else {
                    sb.appendHtmlConstant("<span tabindex=\"-1\"></span>");
                }
                sb.appendHtmlConstant("</td>");
            }

            @Override
            protected Element getContainerElement(Element parent) {
                return parent.getFirstChildElement().getFirstChildElement()
                        .getFirstChildElement();
            }
        };
        return cell;
    }

    /**
     * Get the program and releases from VSAC using REST calls and set it in the MatContext
     * the first time the value sets page is loaded.
     * If the values have been loaded previously, no calls are made.
     */
    public static void getProgramsAndReleases() {

        HashMap<String, List<String>> pgmRelMap = (HashMap<String, List<String>>) MatContext.get().getProgramToReleases();

        if (pgmRelMap == null || pgmRelMap.isEmpty()) {
            MatContext.get().getProgramsAndReleasesFromVSAC();
        }

    }

    public static boolean isFhirUrl(String oid) {
        return oid != null && oid.startsWith("http");
    }

    public static String getOidFromUrl(String oid) {
        String result = oid;
        if (isFhirUrl(oid)) {
            int lastSlash = oid.lastIndexOf("/");
            if (lastSlash >= 0) {
                result = oid.substring(lastSlash + 1);
            } else {
                log.log(Level.INFO, "Couldn't find / assuming urn is an oid.");
            }
        }
        return result;
    }
}


