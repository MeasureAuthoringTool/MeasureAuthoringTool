package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.measure.ReferenceTextAndType;

import java.util.LinkedList;
import java.util.List;

public class ReferencesModel implements MeasureDetailsComponentModel, IsSerializable {
    private List<ReferenceTextAndType> references = new LinkedList<>();

    public ReferencesModel(ReferencesModel originalModel) {
        references = new LinkedList<>();
        if (originalModel != null && originalModel.getReferences() != null) {
            references.addAll(originalModel.getReferences());
        }
    }

    public ReferencesModel() {
    }

    public List<ReferenceTextAndType> getReferences() {
        return references;
    }

    public void setReferences(List<ReferenceTextAndType> references) {
        List<ReferenceTextAndType> referenceList = new LinkedList<>();
        if (references != null) {
            for (ReferenceTextAndType reference : references) {
                referenceList.add(reference);
            }
        }
        this.references = referenceList;
    }

    @Override
    public boolean equals(MeasureDetailsComponentModel model) {
        ReferencesModel originalModel = (ReferencesModel) model;
        if (originalModel.getReferences() != null) {
            return (originalModel.getReferences().size() == getReferences().size()) && originalModel.getReferences().stream().allMatch(ref -> getReferences().contains(ref));
        } else {
            return getReferences().isEmpty();
        }
    }

    public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
        measureDetailsModelVisitor.updateModel(this);
    }

    public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
        return measureDetailsModelVisitor.validateModel(this);
    }

    @Override
    public boolean isDirty(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
        return measureDetailsModelVisitor.isDirty(this);
    }

}
