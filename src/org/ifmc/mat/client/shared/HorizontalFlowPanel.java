package org.ifmc.mat.client.shared;

/**
 * this class can be used in lieu of HorizontalPanel
 * and so avoid using a <table> element
 * @author aschmidt
 *
 */
public class HorizontalFlowPanel extends CustomFlowPanel {
	@Override
	protected String getFlowStyle() {
		return "inline";
	}
}
