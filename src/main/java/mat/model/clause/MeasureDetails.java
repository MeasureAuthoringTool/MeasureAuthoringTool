package mat.model.clause;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "MEASURE_DETAILS", uniqueConstraints = @UniqueConstraint(columnNames = "MEASURE_ID"))
public class MeasureDetails implements IsSerializable{
	
	private int id;
	private String description;
	private String copyright;
	private String disclaimer;
	private String stratification;
	private String riskAdjustment;
	private String rateAggregation;
	private String rationale;
	private String clinicalRecommendation;
	private String improvementNotation;
	private String definition;
	private String guidance;
	private String transmissionFormat;
	private String initialPopulation;
	private String denominator;
	private String denominatorExclusions;
	private String numerator;
	private String numeratorExclusions;
	private String measureObservations;
	private String measurePopulation;
	private String measurePopulationExclusions;
	private String denominatorExceptions;
	private String supplementalDataElements;
	private String measureSet;
	private Measure measure;
	private List<MeasureDetailsReference> measureDetailsReference;
	
	public MeasureDetails() {
		
	}
	
	@Id
    @GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "COPYRIGHT")
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	
	@Column(name = "DISCLAIMER")
	public String getDisclaimer() {
		return disclaimer;
	}
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}
	
	@Column(name = "STRATIFICATION")
	public String getStratification() {
		return stratification;
	}
	public void setStratification(String stratification) {
		this.stratification = stratification;
	}
	
	@Column(name = "RISK_ADJUSTMENT")
	public String getRiskAdjustment() {
		return riskAdjustment;
	}
	public void setRiskAdjustment(String riskAdjustment) {
		this.riskAdjustment = riskAdjustment;
	}
	
	@Column(name = "RATE_AGGREGATION")
	public String getRateAggregation() {
		return rateAggregation;
	}
	public void setRateAggregation(String rateAggregation) {
		this.rateAggregation = rateAggregation;
	}
	
	@Column(name = "RATIONALE")
	public String getRationale() {
		return rationale;
	}
	public void setRationale(String rationale) {
		this.rationale = rationale;
	}
	
	@Column(name = "CLINICAL_RECOMMENDATION")
	public String getClinicalRecommendation() {
		return clinicalRecommendation;
	}
	public void setClinicalRecommendation(String clinicalRecommendation) {
		this.clinicalRecommendation = clinicalRecommendation;
	}
	
	@Column(name = "IMPROVEMENT_NOTATION")
	public String getImprovementNotation() {
		return improvementNotation;
	}
	public void setImprovementNotation(String improvementNotation) {
		this.improvementNotation = improvementNotation;
	}
	
	@Column(name = "DEFINITION")
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	@Column(name = "GUIDANCE")
	public String getGuidance() {
		return guidance;
	}
	public void setGuidance(String guidance) {
		this.guidance = guidance;
	}
	
	@Column(name = "TRANSMISSION_FORMAT")
	public String getTransmissionFormat() {
		return transmissionFormat;
	}
	public void setTransmissionFormat(String transmissionFormat) {
		this.transmissionFormat = transmissionFormat;
	}
	
	@Column(name = "INITIAL_POPULATION")
	public String getInitialPopulation() {
		return initialPopulation;
	}
	public void setInitialPopulation(String initialPopulation) {
		this.initialPopulation = initialPopulation;
	}
	
	@Column(name = "DENOMINATOR")
	public String getDenominator() {
		return denominator;
	}
	public void setDenominator(String denominator) {
		this.denominator = denominator;
	}
	
	@Column(name = "DENOMINATOR_EXCLUSIONS")
	public String getDenominatorExclusions() {
		return denominatorExclusions;
	}
	public void setDenominatorExclusions(String denominatorExclusions) {
		this.denominatorExclusions = denominatorExclusions;
	}
	
	@Column(name = "NUMERATOR")
	public String getNumerator() {
		return numerator;
	}
	public void setNumerator(String numerator) {
		this.numerator = numerator;
	}
	
	@Column(name = "NUMERATOR_EXCLUSIONS")
	public String getNumeratorExclusions() {
		return numeratorExclusions;
	}
	public void setNumeratorExclusions(String numeratorExclusions) {
		this.numeratorExclusions = numeratorExclusions;
	}
	
	@Column(name = "MEASURE_OBSERVATIONS")
	public String getMeasureObservations() {
		return measureObservations;
	}
	public void setMeasureObservations(String measureObservations) {
		this.measureObservations = measureObservations;
	}
	
	@Column(name = "MEASURE_POPULATION")
	public String getMeasurePopulation() {
		return measurePopulation;
	}
	public void setMeasurePopulation(String measurePopulation) {
		this.measurePopulation = measurePopulation;
	}
	
	@Column(name = "MEASURE_POPULATION_EXCLUSIONS")
	public String getMeasurePopulationExclusions() {
		return measurePopulationExclusions;
	}
	public void setMeasurePopulationExclusions(String measurePopulationExclusions) {
		this.measurePopulationExclusions = measurePopulationExclusions;
	}
	
	@Column(name = "DENOMINATOR_EXCEPTIONS")
	public String getDenominatorExceptions() {
		return denominatorExceptions;
	}
	public void setDenominatorExceptions(String denominatorExceptions) {
		this.denominatorExceptions = denominatorExceptions;
	}
	
	@Column(name = "SUPPLEMENTAL_DATA_ELEMENTS")
	public String getSupplementalDataElements() {
		return supplementalDataElements;
	}
	public void setSupplementalDataElements(String supplementalDataElements) {
		this.supplementalDataElements = supplementalDataElements;
	}
	
	@Column(name = "MEASURE_SET")
	public String getMeasureSet() {
		return measureSet;
	}
	public void setMeasureSet(String measureSet) {
		this.measureSet = measureSet;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEASURE_ID", unique = true, nullable = false)
	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "measureDetails",cascade=CascadeType.ALL)
	public List<MeasureDetailsReference> getMeasureDetailsReference() {
		return measureDetailsReference;
	}

	public void setMeasureDetailsReference(List<MeasureDetailsReference> measureDetailsReferences) {
		this.measureDetailsReference = measureDetailsReferences;
	}
}
