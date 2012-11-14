package com.funkr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
import com.funkr.entities.*;

public class FunkrIntegrator {

	private String GetShows(String zip, int radius, String startDate,
			String endDate) {
		
		String response = null;
		String reqUrl = "http://api.jambase.com/search";
		String reqParams = "zip=".concat(zip).concat("&radius=")
				.concat(Integer.toString(radius))
				.concat("&apikey=fphg8ah7b6wgv685pyvvegb7")
				.concat("&startDate=").concat(startDate).concat("&endDate=")
				.concat(endDate);
		try {
			response = com.funkr.utils.WebUtils.sendGetRequest(reqUrl, reqParams);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
		return response;
	}

	private JamBase_Data ParseResponse(String response) {
		XStream stream = new XStream();
		JamBase_Data jamdata = null;
		if (response.contains("No shows found")) {
			System.out.println("no jamz found on jambase");
		} else {

			try {

				stream.alias("event", Event.class);
				stream.addImplicitCollection(JamBase_Data.class, "event", Event.class);
				stream.alias("JamBase_Data", JamBase_Data.class);
				stream.alias("artists", Artists.class);
				stream.alias("artist", Artist.class);

				jamdata = (JamBase_Data) stream.fromXML(response);
				return jamdata;
			} catch (Exception e) {
				System.out.println(e);
				System.out.println(e.getStackTrace().toString());
			}
		}

		return jamdata;
	}

	protected ArrayList<Zip> ZipList() {

		System.out.println("build zip list: begin");
		
		Zip z = null;
		ArrayList<Zip> zips = new ArrayList<Zip>();

		z = new Zip("87101", "Albuquerque");
		zips.add(z);
		z = new Zip("85726", "Tucson");
		zips.add(z);
		z = new Zip("73125", "Oklahoma City");
		zips.add(z);
		z = new Zip("97208", "Portland");
		zips.add(z);
		z = new Zip("89199", "Las Vegas");
		zips.add(z);
		z = new Zip("37230", "Nashville-Davidson6");
		zips.add(z);
		z = new Zip("20090", "Washington1");
		zips.add(z);
		z = new Zip("40231", "Louisville-Jefferson County5");
		zips.add(z);
		z = new Zip("80202", "Denver");
		zips.add(z);
		z = new Zip("02205", "Boston");
		zips.add(z);
		z = new Zip("98108", "Seattle");
		zips.add(z);
		z = new Zip("53203", "Milwaukee");
		zips.add(z);
		z = new Zip("79910", "El Paso");
		zips.add(z);
		z = new Zip("28228", "Charlotte");
		zips.add(z);
		z = new Zip("76161", "Fort Worth");
		zips.add(z);
		z = new Zip("21202", "Baltimore");
		zips.add(z);
		z = new Zip("38101", "Memphis");
		zips.add(z);
		z = new Zip("78710", "Austin");
		zips.add(z);
		z = new Zip("43216", "Columbus");
		zips.add(z);
		z = new Zip("94188", "San Francisco");
		zips.add(z);
		z = new Zip("32203", "Jacksonville");
		zips.add(z);
		z = new Zip("46206", "Indianapolis");
		zips.add(z);
		z = new Zip("48233", "Detroit");
		zips.add(z);
		z = new Zip("95101", "San Jose");
		zips.add(z);
		z = new Zip("75260", "Dallas");
		zips.add(z);
		z = new Zip("92199", "San Diego");
		zips.add(z);
		z = new Zip("78284", "San Antonio");
		zips.add(z);
		z = new Zip("85026", "Phoenix");
		zips.add(z);
		z = new Zip("19104", "Philadelphia");
		zips.add(z);
		z = new Zip("77201", "Houston");
		zips.add(z);
		z = new Zip("60607", "Chicago");
		zips.add(z);
		z = new Zip("90052", "Los Angeles");
		zips.add(z);
		z = new Zip("10199", "New York");
		zips.add(z);

		System.out.println("build zip list: complete");
		
		return zips;
	}

	public FunkrIntegrator() {

		System.out.println("starting FunkrIntegrator");
		
		try {
			// Mongo mongodb = new Mongo("localhost", 27017);
			Mongo mongodb = new Mongo("alex.mongohq.com", 10011);
			
			DB db = mongodb.getDB("app8801249");
			boolean auth = db.authenticate("heroku", "heroku".toCharArray());
			
			DBCollection collection = db.getCollection("funkr");

			String resp = null;
			// String zip = "02113";
			int radius = 150;
			
			DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");

			String endDate = DateTime.now().toString(fmt); // "10/27/2012";
			String startDate = DateTime.now().plusDays(-1).toString(fmt);

			ArrayList<Zip> zips = ZipList();

			for (int x = 0; x < zips.size(); x++) {
				// get xml response from jambase
				System.out.println(String.format("getting data for %s", zips.get(x).getCity()));
				
				resp = GetShows(zips.get(x).getZip(), radius, startDate,
						endDate);

				// Serialize via XStream
				JamBase_Data jmdata = ParseResponse(resp);

				if (jmdata != null) {

					// stream events from java obj to json
					ArrayList<Event> evts = jmdata.getJambase_data();

					System.out.println(String.format("found %s events for %s", evts.size(), zips.get(x)));

					Session session = com.funkr.utils.HiberUtil.getSessionFactory().openSession();
					session.beginTransaction();
				
					
					
					for (int i = 0; i < evts.size(); i++) {

						session.merge(evts.get(i));
						session.merge(evts.get(i).getVenue());
						
						System.out.println(String.format("inserted event %s", evts.get(i).getEvent_id()));
						
						
					}
					session.getTransaction().commit();
					session.flush();
					session.close();
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace(System.out);
		}
	}

	public static void main(String[] args) throws IOException,
			URISyntaxException {

		FunkrIntegrator integ = new FunkrIntegrator();
		
	}

}