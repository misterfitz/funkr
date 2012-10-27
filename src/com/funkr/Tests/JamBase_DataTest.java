package com.funkr.Tests;

import java.io.Writer;
import java.util.ArrayList;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.funkr.entities.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class JamBase_DataTest {

	public static void main(String[] args) throws Exception {

		try {

			// WriteZips();

			// TestJamBase_Data();

			TestHibConfig();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	protected static void TestHibConfig() throws Exception {

		try {
			// A SessionFactory is set up once for an application
			SessionFactory sessionFactory = new Configuration().configure() // configures
																			// settings
																			// from
																			// hibernate.cfg.xml
					.buildSessionFactory();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	protected static void WriteZips() {

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

		XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
			public HierarchicalStreamWriter createWriter(Writer writer) {
				return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
			}
		});
		xstream.alias("zip", Zip.class);
		// OperationFuture<Boolean> setOp = client.set("87101", 0, ""{ "zip" :
		// "87101", "city" : "Albuquerque"}"");

		// TODO: add back write ZIPS to db via hibernate
	}

	protected static void TestJamBase_DataStream() {

		Artist artist = new Artist("testartist1", "big tymaz");
		Artist artist2 = new Artist("testArtist2", "whattupyos");

		ArrayList<Artist> artistArrayList = new ArrayList<Artist>();
		artistArrayList.add(artist);
		artistArrayList.add(artist2);

		Artists artists = new Artists(artistArrayList);

		Venue venue = new Venue("venueID1", "testVenueName1", "testVenueCity",
				"testVenueState", "testVenueZip");

		Event e = new Event("testEventID", artistArrayList, "testEventDate",
				venue, "testTicketUrl", "testVenueUrl");

		ArrayList<Event> events = new ArrayList<Event>();
		events.add(e);

		JamBase_Data jbdata = new JamBase_Data("here is a test result title!",
				events);

		XStream xStream = new XStream(new DomDriver());
		xStream.alias("event", Event.class);
		xStream.addImplicitMap(JamBase_Data.class, "event", Event.class,
				"event");
		xStream.alias("JamBase_Data", JamBase_Data.class);
		xStream.alias("artists", Artists.class);
		xStream.alias("artist", Artist.class);
		System.out.println(xStream.toXML(jbdata));

	}
}