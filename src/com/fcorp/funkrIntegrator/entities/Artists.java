package com.fcorp.funkrIntegrator.entities;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("artists")
public class Artists {
	
	private ArrayList<Artist> artists;

	public Artists(ArrayList<Artist> artists) {
		super();
		this.artists = artists;
	}

	public ArrayList<Artist> getArtists() {
		return artists;
	}

	public void setArtists(ArrayList<Artist> artists) {
		this.artists = artists;
	}
	
}
