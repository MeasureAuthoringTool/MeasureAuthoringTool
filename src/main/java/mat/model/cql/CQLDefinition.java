package mat.model.cql;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Set;

public class CQLDefinition implements CQLExpression, IsSerializable, Cell<CQLDefinition> {
    private String id;
    private String definitionName;
    private String definitionLogic;
    private String context = "Patient";
    private boolean supplDataElement;
    private boolean popDefinition;
    private String commentString = "";
    private String returnType;


    public static class Comparator implements java.util.Comparator<CQLDefinition>, IsSerializable {

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(CQLDefinition o1,
                           CQLDefinition o2) {
            return o1.getName().compareTo(o2.getName());
        }

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return getDefinitionName();
    }

    @Override
    public void setName(String name) {
        setDefinitionName(name);
    }

    @Override
    public String getLogic() {
        return getDefinitionLogic();
    }

    @Override
    public void setLogic(String logic) {
        setDefinitionLogic(logic);
    }


    public String getDefinitionName() {
        return definitionName.trim();
    }

    public void setDefinitionName(String name) {
        this.definitionName = name.trim();
    }

    public String getDefinitionLogic() {
        return definitionLogic.trim();
    }

    public void setDefinitionLogic(String logic) {
        this.definitionLogic = logic.trim();
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isSupplDataElement() {
        return supplDataElement;
    }

    public void setSupplDataElement(boolean supplDataElement) {
        this.supplDataElement = supplDataElement;
    }

    @Override
    public boolean dependsOnSelection() {
        return false;
    }

    @Override
    public Set<String> getConsumedEvents() {
        return null;
    }

    @Override
    public boolean handlesSelection() {
        return false;
    }

    @Override
    public boolean isEditing(com.google.gwt.cell.client.Cell.Context context, Element parent, CQLDefinition value) {
        return false;
    }

    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent, CQLDefinition value,
                               NativeEvent event, ValueUpdater<CQLDefinition> valueUpdater) {

    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context, CQLDefinition value, SafeHtmlBuilder sb) {

    }

    @Override
    public boolean resetFocus(com.google.gwt.cell.client.Cell.Context context, Element parent, CQLDefinition value) {
        return false;
    }

    @Override
    public void setValue(com.google.gwt.cell.client.Cell.Context context, Element parent, CQLDefinition value) {

    }

    @Override
    public String toString() {
        return this.definitionName;
    }

    public boolean isPopDefinition() {
        return popDefinition;
    }

    public void setPopDefinition(boolean popDefinition) {
        this.popDefinition = popDefinition;
    }

    public String getCommentString() {
        return commentString;
    }

    public void setCommentString(String commentString) {
        this.commentString = commentString;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }


}
