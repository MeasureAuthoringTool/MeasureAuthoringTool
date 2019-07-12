package mat.client.measure;

import static org.junit.Assert.*;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.junit.Before;
import org.junit.Test;

import mat.client.shared.ListBoxMVP;
import mat.client.shared.TextAreaWithMaxLength;

public class AbstractNewMeasureViewTest {
	
	private AbstractNewMeasureView measureView = new AbstractNewMeasureView();
	
	private TextAreaWithMaxLength measureNameTextBox = new TextAreaWithMaxLength();
	private TextBox eCQMAbbreviatedTitleTextBox = new TextBox();
	private TextAreaWithMaxLength cqlLibraryNameTextBox = new TextAreaWithMaxLength();
	private ListBoxMVP  measureScoringListBox = new ListBoxMVP();
	private HelpBlock helpBlock = new HelpBlock();
	private FormGroup messageFormGrp = new FormGroup();

		
	@Before
	public void init() {
		measureNameTextBox.setText("hey");
		eCQMAbbreviatedTitleTextBox.setText("something");
		cqlLibraryNameTextBox.setText("else");
		measureScoringListBox.setSelectedIndex(3);
		helpBlock.setText("help");
		messageFormGrp.setValidationState(ValidationState.SUCCESS);
	}
	
	@Test
	public void testClearFields() {
		measureView.clearFields();
		assertEquals("", measureNameTextBox.getText());
		assertEquals("", eCQMAbbreviatedTitleTextBox.getText());
		assertEquals("", cqlLibraryNameTextBox.getText());
		assertEquals(0, measureScoringListBox.getSelectedIndex());
		assertEquals("", measureNameTextBox.getText());
		assertEquals(ValidationState.NONE, messageFormGrp.getValidationState());
	}
	
}
