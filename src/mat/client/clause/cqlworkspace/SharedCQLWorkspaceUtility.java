package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.ListBox;

import mat.client.clause.cqlworkspace.leftNavBar.sections.CQLAppliedValueSetUtility;

public class SharedCQLWorkspaceUtility {
	public final static String MUST_HAVE_PROGRAM_WITH_RELEASE = "Cannot select a release without selecting a program.";
	
	public void loadPrograms(ListBox programBox) {
		CQLAppliedValueSetUtility.loadPrograms(programBox);
	}
	
}
