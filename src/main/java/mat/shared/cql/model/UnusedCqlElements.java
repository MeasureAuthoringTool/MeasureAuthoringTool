package mat.shared.cql.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLQualityDataSetDTO;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnusedCqlElements implements IsSerializable {
    private List<CQLIncludeLibrary> libraries;
    private List<CQLQualityDataSetDTO> valueSets;
    private List<CQLCode> codes;

    public List<CQLIncludeLibrary> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<CQLIncludeLibrary> libraries) {
        this.libraries = libraries;
    }

    public List<CQLQualityDataSetDTO> getValueSets() {
        return valueSets;
    }

    public void setValueSets(List<CQLQualityDataSetDTO> valueSets) {
        this.valueSets = valueSets;
    }

    public List<CQLCode> getCodes() {
        return codes;
    }

    public void setCodes(List<CQLCode> codes) {
        this.codes = codes;
    }

    public boolean haveUnused() {
        return (libraries != null && !libraries.isEmpty()) ||
                (valueSets != null && !valueSets.isEmpty()) ||
                (codes != null && !codes.isEmpty());
    }

   public void clearUnusedNotProcessedForStandAlone() {
        valueSets = null;
        codes = null;
    }
}

