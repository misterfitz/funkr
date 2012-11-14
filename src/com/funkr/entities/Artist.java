package com.funkr.entities;

import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("artist")
public class Artist {
	
	@XStreamAlias("artist_id")
	private String artist_id;
	
	@XStreamAlias("artist_name")
	private String artist_name;
	
	private Set<Event> events;
	
	protected Artist() { }
	
	public Artist(String artist_id, String artist_name) {
		super();
		this.artist_id = artist_id;
		this.artist_name = artist_name;
	}
	
	public String getArtist_id() {
		return artist_id;
	}
	
	public void setArtist_id(String artist_id) {
		this.artist_id = artist_id;
	}
	
	public String getArtist_name() {
		return artist_name;
	}
	
	public void setArtist_name(String artist_name) {
		this.artist_name = artist_name;
	}
	
	public void setEvents(Set<Event> events){
		this.events = events;
	}
	public Set<Event> getEvents(){
		return this.events;
	}
	
}
