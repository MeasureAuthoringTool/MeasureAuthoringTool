package org.ifmc.mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;

public class Measure {
	private Headers headers;
	private List<Exceptions> exceptions = new ArrayList<Exceptions>();;
	private String  ttext;
	private Elementlookup elementlookup;
	private List<Exclusions> denominatorExclusions = new ArrayList<Exclusions>();
	private List<Numerator> numerator = new ArrayList<Numerator>();
	private List<NumeratorExclusions> numeratorExclusions = new ArrayList<NumeratorExclusions>();
	private List<Denominator> denominator = new ArrayList<Denominator>();
	private List<Population> population = new ArrayList<Population>();
	private List<MeasurePopulation> measurePopulation = new ArrayList<MeasurePopulation>();
	private List<MeasureObservation> measureObservation = new ArrayList<MeasureObservation>();
	private List<Stratification> stratification = new ArrayList<Stratification>();
	private Abbreviations abbreviations;
	private SupplementalDataElements supplementalDataElements;

	public Abbreviations getAbbreviations() {
		return abbreviations;
	}
	public void setAbbreviations(Abbreviations abbreviations) {
		this.abbreviations = abbreviations;
	}
	public Headers getHeaders() {
		return headers;
	}
	public void setHeaders (Headers headers ) {
		this.headers = headers;
	}
	public List<Exceptions> getExceptions() {
		return exceptions;
	}
	public void setExceptions (List<Exceptions> exceptions ) {
		this.exceptions = exceptions;
	}
	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public Elementlookup getElementlookup() {
		return elementlookup;
	}
	public void setElementlookup (Elementlookup elementlookup ) {
		this.elementlookup = elementlookup;
	}
	public List<Exclusions> getExclusions() {
		return denominatorExclusions;
	}
	public void setExclusions (List<Exclusions> exclusions ) {
		this.denominatorExclusions = exclusions;
	}
	public List<Numerator> getNumerator() {
		return numerator;
	}
	public void setNumerator (List<Numerator> numerator ) {
		this.numerator = numerator;
	}
	
	public List<NumeratorExclusions> getNumeratorExclusions() {
		return numeratorExclusions;
	}
	public void setNumeratorExclusions (List<NumeratorExclusions> numeratorExclusions ) {
		this.numeratorExclusions = numeratorExclusions;
	}
	
	
	public List<Denominator> getDenominator() {
		return denominator;
	}
	public void setDenominator (List<Denominator> denominator ) {
		this.denominator = denominator;
	}
	public List<Population> getPopulation() {
		return population;
	}
	public void setPopulation (List<Population> population ) {
		this.population = population;
	}
	public void setCriterion (Criterion criterion) {
		if (criterion instanceof Population) {
			population.add((Population)criterion);
		} else if (criterion instanceof Numerator) {
			numerator.add((Numerator)criterion);
		} else if (criterion instanceof NumeratorExclusions) {
			numeratorExclusions.add((NumeratorExclusions)criterion);
		} else if (criterion instanceof Denominator) {
			denominator.add((Denominator)criterion);
		} else if (criterion instanceof Exceptions) {
			exceptions.add((Exceptions)criterion);
		} else if (criterion instanceof Exclusions) {
			denominatorExclusions.add((Exclusions)criterion);
		}else if (criterion instanceof MeasurePopulation){
			measurePopulation.add((MeasurePopulation)criterion);
		}else if(criterion instanceof MeasureObservation){
			measureObservation.add((MeasureObservation)criterion);
		}else if(criterion instanceof Stratification){
			stratification.add((Stratification)criterion);
		}
		
	}
	public List<MeasurePopulation> getMeasurePopulation() {
		return measurePopulation;
	}
	public void setMeasurePopulation(List<MeasurePopulation> measurePopulation) {
		this.measurePopulation = measurePopulation;
	}
	public List<MeasureObservation> getMeasureObservation() {
		return measureObservation;
	}
	public void setMeasureObservation(List<MeasureObservation> measureObservation) {
		this.measureObservation = measureObservation;
	}
	public SupplementalDataElements getSupplementalDataElements() {
		return supplementalDataElements;
	}
	public void setSupplementalDataElements(
			SupplementalDataElements supplementalDataElements) {
		this.supplementalDataElements = supplementalDataElements;
	}
	public List<Stratification> getStratification() {
		return stratification;
	}
	public void setStratification(List<Stratification> stratification) {
		this.stratification = stratification;
	}
	
	
	
}