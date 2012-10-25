package com.fcorp.funkrIntegrator.entities;

import java.util.ArrayList;

public class Event {
	
	private String event_id;
	private ArrayList<Artist> artists;
	
	//private Artists artists;
	private String event_date;
	private Venue venue;
	private String ticket_url;
	private String event_url;
	
	public Event(String event_id, ArrayList<Artist> artists, String event_date,
			Venue venue, String ticket_url, String event_url) {
		super();
		this.event_id = event_id;
		this.artists = artists;
		this.event_date = event_date;
		this.venue = venue;
		this.ticket_url = ticket_url;
		this.event_url = event_url;
	}
	
	public String getEvent_id() {
		return event_id;
	}
	
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	
	public ArrayList<Artist> getArtists() {
		return artists;
	}
	
	public void setArtists(ArrayList<Artist> artists) {
		this.artists = artists;
	}
	
	public String getEvent_date() {
		return event_date;
	}
	
	public void setEvent_date(String event_date) {
		this.event_date = event_date;
	}
	
	public Venue getVenue() {
		return venue;
	}
	
	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	
	public String getTicket_url() {
		return ticket_url;
	}
	
	public void setTicket_url(String ticket_url) {
		this.ticket_url = ticket_url;
	}
	
	public String getEvent_url() {
		return event_url;
	}
	
	public void setEvent_url(String event_url) {
		this.event_url = event_url;
	}

}
