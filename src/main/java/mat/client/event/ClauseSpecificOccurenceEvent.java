package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import mat.client.clause.clauseworkspace.model.CellTreeNode;

/**
 * The Class ClauseSpecificOccurenceEvent.
 */
public class ClauseSpecificOccurenceEvent extends GwtEvent<ClauseSpecificOccurenceEvent.Handler> {
	
	private CellTreeNode selectedNode;
	private boolean isOccurrenceCreated;
	
	/**
	 * @return the selectedNode
	 */
	public CellTreeNode getSelectedNode() {
		return selectedNode;
	}
	
	/**
	 * @param selectedNode the selectedNode to set
	 */
	public void setSelectedNode(CellTreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}
	
	/**
	 * @return the isOccurrenceCreated
	 */
	public boolean isOccurrenceCreated() {
		return isOccurrenceCreated;
	}

	/**
	 * @param isOccurrenceCreated the isOccurrenceCreated to set
	 */
	public void setOccurrenceCreated(boolean isOccurrenceCreated) {
		this.isOccurrenceCreated = isOccurrenceCreated;
	}

	/** The type. */
	public static com.google.gwt.event.shared.GwtEvent.Type<Handler> TYPE =
			new GwtEvent.Type<ClauseSpecificOccurenceEvent.Handler>();
	
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On deletion.
		 * 
		 * @param event
		 *            the event
		 */
		public void onSave(ClauseSpecificOccurenceEvent event);
	}
	
	/**
	 * Instantiates a new Clause Specific Occurrence Save event.
	 * 
	 * @param selectedNode
	 *            CellTreeNode
	 */
	public ClauseSpecificOccurenceEvent(CellTreeNode selectedNode, boolean isOccurrenceCreated){
		this.selectedNode = selectedNode;
		this.setOccurrenceCreated(isOccurrenceCreated);
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(Handler handler) {
		handler.onSave(this);
		
	}
}
