package mat.client.cqlworkspace.attributes;

public class AttributesCQLBuilderImpl implements AttributesCQLBuilder {
    public String buildCQL(InsertFhirAttributesDialogModel model) {
        StringBuilder cql = new StringBuilder();
        for (FhirDataTypeModel dataType : model.getDataTypesByResource().values()) {
            if (!dataType.isSelected()) {
                continue;
            }
            for (FhirAttributeModel attribute : dataType.getAttributesByElement().values()) {
                if (!attribute.isSelected()) {
                    continue;
                }
                if (cql.length() != 0) {
                    cql.append("\n");
                }
                cql.append(dataType.getFhirResource()).append(".").append(attribute.getFhirElement());
            }
        }
        return cql.toString();
    }
}
