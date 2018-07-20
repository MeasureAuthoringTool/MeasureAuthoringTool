package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gwtbootstrap3.client.ui.ListBox;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;

import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.util.CellTableUtility;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.ConstantMessages;

public class CQLAppliedValueSetUtility {
	
	public String getExpansionProfileValue(ListBox inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}
	
	public String getDataTypeText(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getItemText(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}

	public String getDataTypeValue(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}
	
	public static boolean checkForEnable() {
		return MatContext.get().getMeasureLockService()
				.checkForEditPermission();
	}
	
	public static Column<CQLQualityDataSetDTO, SafeHtml> createOIDColumn() {
		Column<CQLQualityDataSetDTO, SafeHtml> oidColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLQualityDataSetDTO object) {
				StringBuilder title = new StringBuilder();
				String oid = null;
				if (object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
					title.append("OID : ").append(ConstantMessages.USER_DEFINED_QDM_NAME);
					oid = ConstantMessages.USER_DEFINED_CONTEXT_DESC;
				} else {
					title.append("OID : ").append(object.getOid());
					oid = object.getOid();
				}
				return getOIDColumnToolTip(oid, title, object.isHasModifiedAtVSAC(), object.isNotFoundInVSAC());
			}
		};
		return oidColumn;
	}
	
	public static Column<CQLQualityDataSetDTO, SafeHtml> createProgramColumn() {
		Column<CQLQualityDataSetDTO, SafeHtml> programColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLQualityDataSetDTO object) {
				StringBuilder title = new StringBuilder();
				String program = null;
				if (!object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
					program = (object.getProgram() == null ? "" : object.getProgram());
					title.append("Program : ").append(program);
				} else {
					program = "";
				}
				return CellTableUtility.getColumnToolTip(program, title.toString());
			}
		};
		return programColumn;
	}
	
	public static Column<CQLQualityDataSetDTO, SafeHtml> createReleaseColumn() {
		Column<CQLQualityDataSetDTO, SafeHtml> releaseColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLQualityDataSetDTO object) {
				StringBuilder title = new StringBuilder();
				String release = "";
				if (!object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
					title.append("Release : ").append(object.getRelease());
					release = object.getRelease() != null ? object.getRelease() : "";
				}
				return CellTableUtility.getColumnToolTip(release, title.toString());
			}
		};
		return releaseColumn;
	}
	
	public static void initializeReleaseListBoxContent(ListBox releaseBox) {
		releaseBox.clear();
		releaseBox.setEnabled(true);
		releaseBox.addItem(MatContext.PLEASE_SELECT, MatContext.PLEASE_SELECT);
	}
		
	public static void loadPrograms(ListBox programBox) {
		programBox.clear();
		programBox.addItem(MatContext.PLEASE_SELECT, MatContext.PLEASE_SELECT);
		HashMap<String, List<String>> pgmRelMap = (HashMap<String, List<String>>) MatContext.get().getProgramToReleases(); 
		pgmRelMap.forEach((k, v) -> programBox.addItem(k));
	}
	
	//loadPrograms() MUST be called before loadReleases()
	public static void loadReleases(ListBox releaseBox, ListBox programBox) {
		releaseBox.setEnabled(false);
		List<String> releases = new ArrayList<>();
		releases.add(MatContext.PLEASE_SELECT);
		String program = programBox.getSelectedValue();
		if(!program.equals(MatContext.PLEASE_SELECT)) {
			releaseBox.setEnabled(true);
			releases.addAll(MatContext.get().getProgramToReleases().get(programBox.getSelectedValue()));
			releaseBox.clear();
			for(String release : releases) {
				releaseBox.addItem(release, release);
			}
		}
	}
	
	
	public static void loadProgramsAndReleases(ListBox programs, ListBox releases) {
		loadPrograms(programs);
		loadReleases(releases, programs);
	}
	
	private static SafeHtml getOIDColumnToolTip(String columnText,
			StringBuilder title, boolean hasImage, boolean isUserDefined) {
		if (hasImage && !isUserDefined) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/bullet_tick.png\" alt=\"Value set is updated from VSAC.\""
					+ "title = \"Value set is updated from VSAC.\"/>"
					+ "<span tabIndex = \"0\" title='" + title + "'>"
					+ columnText + "</span></body>" + "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant)
					.toSafeHtml();
		} else if (hasImage && isUserDefined) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/userDefinedWarning.png\""
					+ "alt=\"Warning : Value set is not available in VSAC.\""
					+ " title=\"Value set is not available in VSAC.\"/>"
					+ "<span tabIndex = \"0\" title='" + title + "'>"
					+ columnText + "</span></body>" + "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant)
					.toSafeHtml();
		} else {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><span tabIndex = \"0\" title='"
					+ title + "'>" + columnText + "</span></body>" + "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant)
					.toSafeHtml();
		}
	}
}
