package com.redhat.droolsfeature.model;

import java.util.ArrayList;

public class BzrFlightDeclare {
	private int lom; 
	private String airline; 
	private ArrayList<BzrFlightMatchDeclare> matchList; 
	
	
	/**
	 * @return the lom
	 */
	public int getLom() {
		return lom;
	}
	/**
	 * @param lom the lom to set
	 */
	public void setLom(int lom) {
		this.lom = lom;
	}
	/**
	 * @return the airline
	 */
	public String getAirline() {
		return airline;
	}
	/**
	 * @param airline the airline to set
	 */
	public void setAirline(String airline) {
		this.airline = airline;
	}
	/**
	 * @return the matchList
	 */
	public ArrayList<BzrFlightMatchDeclare> getMatchList() {
		return matchList;
	}
	/**
	 * @param matchList the matchList to set
	 */
	public void setMatchList(ArrayList<BzrFlightMatchDeclare> matchList) {
		this.matchList = matchList;
	}

    
    
	
	
}
