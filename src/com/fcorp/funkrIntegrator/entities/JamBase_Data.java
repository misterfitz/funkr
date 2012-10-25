package com.fcorp.funkrIntegrator.entities;

import java.util.ArrayList;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("JamBase_Data")
public class JamBase_Data {	
	
	@XStreamAlias("Results_Title")
	private String Results_Title;
	
	//@XStreamImplicit(itemFieldName="JamBase_Data")
	@XStreamImplicit(itemFieldName="event")
	private ArrayList<Event> event;
	
	public JamBase_Data(String results_Title, ArrayList<Event> events) {
		super();
		Results_Title = results_Title;
		this.event = events;
	}
	
	public String getResults_Title() {
		return Results_Title;
	}
	
	public void setResults_Title(String results_Title) {
		Results_Title = results_Title;
	}
	
	public ArrayList<Event> getJambase_data() {
		return this.event;
	}
	
	public void setJambase_data(ArrayList<Event> events) {
		this.event = events;
	}
}
