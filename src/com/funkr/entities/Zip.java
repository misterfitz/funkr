package com.funkr.entities;

public class Zip {

	public Zip(String zip, String city){
		this.setZip(zip);
		this.setCity(city);
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	private String zip;
	private String city;
	
}
