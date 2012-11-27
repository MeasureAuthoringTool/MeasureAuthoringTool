package org.ifmc.mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;

public class Scores {
	private String  ttext;
	private List<Score> scores;

	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public List<Score> getScores() {
		if(scores == null)
			scores = new ArrayList<Score>();
		return scores;
	}
	public void setScores(List<Score> scores) {
		this.scores = scores;
	}
	
}