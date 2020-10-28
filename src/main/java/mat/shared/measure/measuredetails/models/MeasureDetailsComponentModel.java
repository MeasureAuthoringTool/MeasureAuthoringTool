package mat.shared.measure.measuredetails.models;

import java.util.List;

public interface MeasureDetailsComponentModel {
    boolean equals(MeasureDetailsComponentModel model);

    void update(MeasureDetailsModelVisitor measureDetailsModelVisitor);

    List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor);

    boolean isDirty(MeasureDetailsModelVisitor measureDetailsModelVisitor);
}
