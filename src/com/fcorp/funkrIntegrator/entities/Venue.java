package com.fcorp.funkrIntegrator.entities;

public class Venue {
	public Venue(String venue_id, String venue_name, String venue_city,
			String venue_state, String venue_zip) {
		super();
		this.venue_id = venue_id;
		this.venue_name = venue_name;
		this.venue_city = venue_city;
		this.venue_state = venue_state;
		this.venue_zip = venue_zip;
	}
	private String venue_id;
	public String getVenue_id() {
		return venue_id;
	}
	public void setVenue_id(String venue_id) {
		this.venue_id = venue_id;
	}
	private String venue_name;
	public String getVenue_name() {
		return venue_name;
	}
	public void setVenue_name(String venue_name) {
		this.venue_name = venue_name;
	}
	public String getVenue_city() {
		return venue_city;
	}
	public void setVenue_city(String venue_city) {
		this.venue_city = venue_city;
	}
	public String getVenue_state() {
		return venue_state;
	}
	public void setVenue_state(String venue_state) {
		this.venue_state = venue_state;
	}
	public String getVenue_zip() {
		return venue_zip;
	}
	public void setVenue_zip(String venue_zip) {
		this.venue_zip = venue_zip;
	}
	private String venue_city;
	private String venue_state;
	private String venue_zip;
	
}
