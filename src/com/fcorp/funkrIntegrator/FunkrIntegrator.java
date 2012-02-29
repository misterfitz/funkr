package com.fcorp.funkrIntegrator;

import java.util.List;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import com.fcorp.funkrIntegrator.entities.Artist;
import com.fcorp.funkrIntegrator.entities.Artists;
import com.fcorp.funkrIntegrator.entities.Event;
import com.fcorp.funkrIntegrator.entities.JamBase_Data;
import com.fcorp.funkrIntegrator.entities.Zip;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class FunkrIntegrator {

	/**
	 * @param args
	 */

	protected static String GetShows(String zip, int radius, String startDate,
			String endDate) {
		String response = null;
		String reqUrl = "http://api.jambase.com/search";
		String reqParams = "zip=".concat(zip).concat("&radius=")
				.concat(Integer.toString(radius))
				.concat("&apikey=fphg8ah7b6wgv685pyvvegb7")
				.concat("&startDate=").concat(startDate).concat("&endDate=")
				.concat(endDate);

		// String response = null;
		// String reqUrl = "http://api.jambase.com/search";
		// String reqParams =
		// "zip=02115&radius=75&apikey=fphg8ah7b6wgv685pyvvegb7&startDate=2/19/12&endDate=2/21/12";
		// response = sendGetRequest(reqUrl, reqParams);
		try {
			response = sendGetRequest(reqUrl, reqParams);

			System.out.println(response);

		} catch (Exception e) {

		}

		return response;

	}

	protected static JamBase_Data ParseResponse(String response) {
		XStream stream = new XStream();
		JamBase_Data jamdata = null;
		if (response.contains("No shows found")) {
			System.out.println("no jamz found on jambase");
		} else {

			try {
				/*
				 * stream.alias("JamBase_Data", JamBase_Data.class);
				 * stream.alias("event", Event.class);
				 * stream.addImplicitCollection(Event.class, "event");
				 * stream.alias("artists", Artists.class);
				 * stream.alias("artist", Artist.class);
				 * stream.addImplicitCollection(Artists.class, "artists");
				 * stream.alias("venue", Venue.class);
				 */

				stream.alias("event", Event.class);
				stream.addImplicitMap(JamBase_Data.class, "event", Event.class,
						"event");
				stream.alias("JamBase_Data", JamBase_Data.class);
				stream.alias("artists", Artists.class);
				stream.alias("artist", Artist.class);

				jamdata = (JamBase_Data) stream.fromXML(response);
				System.out.println(jamdata.toString());
				return jamdata;

			}

			catch (Exception e) {
				System.out.println(e);
				System.out.println(e.getStackTrace().toString());
			}
		}

		return jamdata;
	}

	

	/*
	 * protected static ArrayList<Zip> GetZips(){ ArrayList<Zip> getZips = new
	 * ArrayList<Zip>();
	 * 
	 * CouchbaseClient cbClient = GetCouchbaseClient();
	 * 
	 * return getZips; }
	 */

	public static void main(String[] args) throws IOException,
			URISyntaxException {
		// TODO Auto-generated method stub

		String resp = null;
		String zip = "02215";
		int radius = 150;
		String startDate = "2/22/2012";
		String endDate = "3/14/2012";

		// ** need to get all zips

		// ArrayList<Zip> zips = GetZips();

		// get xml response from jambase
		resp = GetShows(zip, radius, startDate, endDate);

		// serililizae via XStream
		JamBase_Data jmdata = ParseResponse(resp);

		
		//CouchbaseClient cbClient = GetCouchbaseClient();

		if (jmdata != null) {

			int EXP_TIME = 10;
			Boolean do_delete = false;

			// stream events from java obj to json
			ArrayList<Event> evts = jmdata.getJambase_data();

			// new streamer
			XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
				public HierarchicalStreamWriter createWriter(Writer writer) {
					return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
				}
			});
			xstream.alias("Event", Event.class);

			// Do an asynchronous set FOR ALL
			// OperationFuture<Boolean> setOp = client.set(KEY, EXP_TIME,
			// VALUE);
			for (int i = 0; i < evts.size(); i++) {

				// System.out.println(xstream.toXML(evts.get(i)));
				//OperationFuture<Boolean> setOp = cbClient.set(evts.get(i)
						//.getEvent_id(), 0, xstream.toXML(evts.get(i)));
				System.out.println(xstream.toXML(evts.get(i)));
			}

			for (int i = 0; i < evts.size(); i++) {
				// Do an asynchronous get FOR ALL
//				GetFuture getOp = cbClient.asyncGet(evts.get(i).getEvent_id());

				// Do an asynchronous delete
	//			OperationFuture<Boolean> delOp = null;
				if (do_delete) {
		//			delOp = cbClient.delete(evts.get(i).getEvent_id());
				}
			}

			// Shutdown the client
			//cbClient.shutdown(3, TimeUnit.SECONDS);
		}

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

	protected static void CouchDbScraps() {
		// Now we want to see what happened with our data
		// Check to see if our set succeeded
		// try {
		// if (setOp.get().booleanValue()) {
		// System.out.println("Set Succeeded");
		// } else {
		// System.err.println("Set failed: "
		// + setOp.getStatus().getMessage());
		// }
		// } catch (Exception e) {
		// System.err.println("Exception while doing set: "
		// + e.getMessage());
		// }
		// // Print the value from synchronous get
		// if (getObject != null) {
		// System.out.println("Synchronous Get Suceeded: "
		// + (String) getObject);
		// } else {
		// System.err.println("Synchronous Get failed");
		// }
		// // Check to see if ayncGet succeeded
		// try {
		// if ((getObject = getOp.get()) != null) {
		// System.out.println("Asynchronous Get Succeeded: "
		// + getObject);
		// } else {
		// System.err.println("Asynchronous Get failed: "
		// + getOp.getStatus().getMessage());
		// }
		// } catch (Exception e) {
		// System.err.println("Exception while doing Aynchronous Get: "
		// + e.getMessage());
		// }
		// // Check to see if our delete succeeded
		// if (do_delete) {
		// try {
		// if (delOp.get().booleanValue()) {
		// System.out.println("Delete Succeeded");
		// } else {
		// System.err.println("Delete failed: "
		// + delOp.getStatus().getMessage());
		// }
		// } catch (Exception e) {
		// System.err.println("Exception while doing delete: "
		// + e.getMessage());
		// }
		// }

	}
}