package org.ifmc.mat.DTO;


import org.ifmc.mat.client.codelist.HasListBox;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * DTO for Measure Score table attributes
 *
 */
public class MeasureScoreDTO implements IsSerializable, HasListBox {

	private String id;
	private String score;
	
	public MeasureScoreDTO(){
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getValue() {
		return id;
	}
	public String getItem() {
		return score;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	@Override
	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
