package com.fcorp.funkrIntegrator;

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
import com.fcorp.funkrIntegrator.entities.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

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
			response = sendGetRequest(reqUrl, reqParams);
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
				stream.addImplicitMap(JamBase_Data.class, "event", Event.class,
						"event");
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

	public BasicDBObject BuildDbDoc(Event evt) {

		BasicDBObject eventDoc = new BasicDBObject();

		eventDoc.put("event_id", evt.getEvent_id());
		eventDoc.put("event_date", evt.getEvent_date());
		eventDoc.put("ticket_url", evt.getTicket_url());
		eventDoc.put("event_url", evt.getEvent_url());

		// build artists
		BasicDBObject artistsDoc = new BasicDBObject();
		ArrayList<Artist> artists = evt.getArtists();
		for (int i = 0; i < artists.size(); i++) {
			BasicDBObject artistDoc = new BasicDBObject();
			artistDoc.put("artist_id", artists.get(i).getArtist_id());
			artistDoc.put("artist_name", artists.get(i).getArtist_name());
			artistsDoc.put("artist", artistDoc);
		}

		eventDoc.put("artists", artistsDoc);

		// build venue
		BasicDBObject venueDoc = new BasicDBObject();
		venueDoc.put("id", evt.getVenue().getVenue_id());
		venueDoc.put("name", evt.getVenue().getVenue_name());
		venueDoc.put("city", evt.getVenue().getVenue_city());
		venueDoc.put("state", evt.getVenue().getVenue_state());
		venueDoc.put("zip", evt.getVenue().getVenue_zip());

		eventDoc.put("venue", venueDoc);

		return eventDoc;

	}

	protected ArrayList<Zip> ZipList() {

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

		return zips;
	}

	public FunkrIntegrator() {

		try {
			Mongo mongodb = new Mongo("localhost", 27017);
			DB db = mongodb.getDB("test");
			DBCollection collection = db.getCollection("funkr");

			String resp = null;
			// String zip = "02113";
			int radius = 150;
			String startDate = "10/24/2012";
			String endDate = "10/25/2012";

			ArrayList<Zip> zips = ZipList();

			for (int x = 0; x < zips.size(); x++) {
				// get xml response from jambase
				resp = GetShows(zips.get(x).getZip(), radius, startDate,
						endDate);

				// Serialize via XStream
				JamBase_Data jmdata = ParseResponse(resp);

				if (jmdata != null) {

					// stream events from java obj to json
					ArrayList<Event> evts = jmdata.getJambase_data();

					// new streamer
					XStream xstream = new XStream(
							new JsonHierarchicalStreamDriver() {
								public HierarchicalStreamWriter createWriter(
										Writer writer) {
									return new JsonWriter(writer,
											JsonWriter.DROP_ROOT_MODE);
								}
							});
					xstream.alias("Event", Event.class);

					for (int i = 0; i < evts.size(); i++) {

						// System.out.println(xstream.toXML(evts.get(i)));
						// XStream jsonStream = new XStream(new
						// JettisonMappedXmlDriver());
						// jsonStream.setMode(XStream.NO_REFERENCES);
						// jsonStream.alias("event", Event.class);
						// System.out.println(jsonStream.toXML(evts.get(i)));

						collection.insert(BuildDbDoc(evts.get(i)));

					}

				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static void main(String[] args) throws IOException,
			URISyntaxException {

		FunkrIntegrator integ = new FunkrIntegrator();

	}

	/**
	 * Sends an HTTP GET request to a url
	 * 
	 * @param endpoint
	 *            - The URL of the server. (Example:
	 *            " http://www.yahoo.com/search")
	 * @param requestParameters
	 *            - all the request parameters (Example:
	 *            "param1=val1&param2=val2"). Note: This method will add the
	 *            question mark (?) to the request - DO NOT add it yourself
	 * @return - The response from the end point
	 */
	public static String sendGetRequest(String endpoint,
			String requestParameters) {
		String result = null;
		if (endpoint.startsWith("http://")) {
			// Send a GET request to the servlet
			try {
				// Send data
				String urlStr = endpoint;
				if (requestParameters != null && requestParameters.length() > 0) {
					urlStr += "?" + requestParameters;
				}
				URL url = new URL(urlStr);
				URLConnection conn = url.openConnection();
				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
				result = sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Reads data from the data reader and posts it to a server via POST
	 * request. data - The data you want to send endpoint - The server's address
	 * output - writes the server's response to output
	 * 
	 * @throws Exception
	 */
	public static void postData(Reader data, URL endpoint, Writer output)
			throws Exception {
		HttpURLConnection urlc = null;
		try {
			urlc = (HttpURLConnection) endpoint.openConnection();
			try {
				urlc.setRequestMethod("POST");
			} catch (ProtocolException e) {
				throw new Exception(
						"Shouldn't happen: HttpURLConnection doesn't support POST??",
						e);
			}
			urlc.setDoOutput(true);
			urlc.setDoInput(true);
			urlc.setUseCaches(false);
			urlc.setAllowUserInteraction(false);
			urlc.setRequestProperty("Content-type", "text/xml; charset="
					+ "UTF-8");
			OutputStream out = urlc.getOutputStream();
			try {
				Writer writer = new OutputStreamWriter(out, "UTF-8");
				pipe(data, writer);
				writer.close();
			} catch (IOException e) {
				throw new Exception("IOException while posting data", e);
			} finally {
				if (out != null)
					out.close();
			}
			InputStream in = urlc.getInputStream();
			try {
				Reader reader = new InputStreamReader(in);
				pipe(reader, output);
				reader.close();
			} catch (IOException e) {
				throw new Exception("IOException while reading response", e);
			} finally {
				if (in != null)
					in.close();
			}
		} catch (IOException e) {
			throw new Exception("Connection error (is server running at "
					+ endpoint + " ?): " + e);
		} finally {
			if (urlc != null)
				urlc.disconnect();
		}
	}

	/**
	 * Pipes everything from the reader to the writer via a buffer
	 */
	private static void pipe(Reader reader, Writer writer) throws IOException {
		char[] buf = new char[1024];
		int read = 0;
		while ((read = reader.read(buf)) >= 0) {
			writer.write(buf, 0, read);
		}
	}
}