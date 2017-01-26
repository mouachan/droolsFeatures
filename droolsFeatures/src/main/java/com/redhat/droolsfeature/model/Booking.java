package com.redhat.droolsfeature.model;

public class Booking {

    private String customer;
    private BzrFlightDeclare flight; 
    private BzrFlightDeclare returnFlight;
	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}
	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	/**
	 * @return the flight
	 */
	public BzrFlightDeclare getFlight() {
		return flight;
	}
	/**
	 * @param flight the flight to set
	 */
	public void setFlight(BzrFlightDeclare flight) {
		this.flight = flight;
	}
	/**
	 * @return the returnFlight
	 */
	public BzrFlightDeclare getReturnFlight() {
		return returnFlight;
	}
	/**
	 * @param returnFlight the returnFlight to set
	 */
	public void setReturnFlight(BzrFlightDeclare returnFlight) {
		this.returnFlight = returnFlight;
	}  
}
