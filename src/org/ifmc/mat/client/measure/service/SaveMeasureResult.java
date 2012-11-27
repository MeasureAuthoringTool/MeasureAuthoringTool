package org.ifmc.mat.client.measure.service;

import java.util.List;

import org.ifmc.mat.client.shared.SuccessFailureHolder;
import org.ifmc.mat.model.Author;
import org.ifmc.mat.model.MeasureType;
import org.ifmc.mat.shared.ConstantMessages;

public class SaveMeasureResult extends SuccessFailureHolder {
	public static final int ID_NOT_UNIQUE = ConstantMessages.ID_NOT_UNIQUE;
	public static final int REACHED_MAXIMUM_VERSION = ConstantMessages.REACHED_MAXIMUM_VERSION;
	public static final int REACHED_MAXIMUM_MAJOR_VERSION = ConstantMessages.REACHED_MAXIMUM_MAJOR_VERSION;
	public static final int REACHED_MAXIMUM_MINOR_VERSION = ConstantMessages.REACHED_MAXIMUM_MINOR_VERSION;
	public static final int INVALID_VALUE_SET_DATE = ConstantMessages.INVALID_VALUE_SET_DATE;
	private String id;
	
	private List<Author> authorList;
	private List<MeasureType> measureTypeList;
	private String versionStr;
	
	public List<Author> getAuthorList() {
		return authorList;
	}
	public void setAuthorList(List<Author> authorList) {
		this.authorList = authorList;
	}
	public List<MeasureType> getMeasureTypeList() {
		return measureTypeList;
	}
	public void setMeasureTypeList(List<MeasureType> measureTypeList) {
		this.measureTypeList = measureTypeList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getVersionStr() {
		return versionStr;
	}
	public void setVersionStr(String versionStr) {
		this.versionStr = versionStr;
	}
	
}
