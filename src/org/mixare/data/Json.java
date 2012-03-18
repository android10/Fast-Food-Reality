/*
 * Copyright (C) 2010- Peer internet solutions
 *
 * This file is part of mixare.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package org.mixare.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mixare.MixView;

import android.text.Html;
import android.util.Log;

public class Json extends DataHandler {

	public void processBuzzJSONObject(JSONObject jo) throws NumberFormatException, JSONException {
		if (jo.has("title") && jo.has("geocode") && jo.has("links")) {
			//Log.d(MixView.TAG, "processing Google Buzz JSON data");
			createMarker( jo.getString("title"),
					Double.valueOf(jo.getString("geocode").split(" ")[0]),
					Double.valueOf(jo.getString("geocode").split(" ")[1]),0,
					jo.getJSONObject("links").getJSONArray("alternate").getJSONObject(0).getString("href"));
		}
	}

	public void processTwitterJSONObject(JSONObject jo) throws NumberFormatException, JSONException {
		if (jo.has("geo")&& !jo.isNull("geo")) {
			//Log.d(MixView.TAG, "processing Twitter JSON data");
			JSONObject geo = jo.getJSONObject("geo");
			JSONArray coordinates = geo.getJSONArray("coordinates");

			createMarker( jo.getString("text"),
					Double.parseDouble(coordinates.getString(0)),
					Double.parseDouble(coordinates.getString(1)),
					0,null);
		}
	}

	public void processMixareJSONObject(JSONObject jo) throws JSONException {

		if (jo.has("title") && jo.has("lat") && jo.has("lng") && jo.has("elevation") && jo.has("has_detail_page")) {

			//Log.d(MixView.TAG, "processing Mixare JSON data");
			String link=null;

			if(jo.getInt("has_detail_page")!=0 && jo.has("webpage"))
				link=jo.getString("webpage");

			createMarker( jo.getString("title"),
					jo.getDouble("lat"),
					jo.getDouble("lng"),
					jo.getDouble("elevation"),
					link);

		}
	}

	public void processWikipediaJSONObject(JSONObject jo) throws JSONException {

		if (jo.has("title") && jo.has("lat") && jo.has("lng") && jo.has("elevation") && jo.has("wikipediaUrl")) {

			//Log.d(MixView.TAG, "processing Wikipedia JSON data");
			createMarker( jo.getString("title"),
					jo.getDouble("lat"),
					jo.getDouble("lng"),
					jo.getDouble("elevation"),
					"http://"+jo.getString("wikipediaUrl"));
		}
	}
	
	//android10 added
	public void processAndroid10JSONObject(JSONObject jo) throws JSONException {
		
		String title = "";
		String address = "";
		String phonenumber = "";
		Double latitude;
		Double longitude;
		Double elevation;
		String webpage = "";
		
		JSONObject phones = null;
		JSONArray phonenumbers = null;
		
		if (jo.has("lat") && jo.has("lng") && jo.has("titleNoFormatting") && jo.has("streetAddress")) {

			Log.d(MixView.TAG, "processing android10 JSON data");
			
			if (jo.has("phoneNumbers")){
				phonenumbers = jo.getJSONArray("phoneNumbers");				
				if (phonenumbers.length() >= 1) {
					phones = phonenumbers.getJSONObject(0);
					if (phones != null)
						phonenumber = phones.getString("number");
				}
			}
			
			
			title = jo.getString("titleNoFormatting");
			title = Html.fromHtml(title).toString(); //to decode Google response
			address = jo.getString("streetAddress");			
			latitude = jo.getDouble("lat");
			longitude = jo.getDouble("lng");
			elevation = Double.parseDouble("120");
			webpage = "google.streetview:cbll=" + 
					  Double.toString(latitude) + 
					  "," + 
					  Double.toString(longitude) + 
					  "&cbp=1,-60.00,,1,-1.0&mz=0";		
			
			createMarker(title,
						 address,
						 phonenumber,
					     latitude,
					     longitude,
					     elevation,
					     webpage);
		}
	}

	public void load(JSONObject root) {
		JSONObject jo = null;
		JSONArray dataArray = null;

		try {
			// Twitter & own schema
			if(root.has("results"))
				dataArray = root.getJSONArray("results");
			// Android 10
			else if (root.has("responseData"))
				dataArray = root.getJSONObject("responseData").getJSONArray("results");
			// Wikipedia
			else if (root.has("geonames"))
				dataArray = root.getJSONArray("geonames");
			// Google Buzz
			else if (root.has("data") && root.getJSONObject("data").has("items"))
				dataArray = root.getJSONObject("data").getJSONArray("items");
			if (dataArray != null) {

				Log.i(MixView.TAG, "processing JSON Data Array");
				int top = Math.min(50, dataArray.length());

				for (int i = 0; i < top; i++) {
					jo = dataArray.getJSONObject(i);

					processAndroid10JSONObject(jo);
					//processMixareJSONObject(jo);
					//processWikipediaJSONObject(jo);
					//processTwitterJSONObject(jo);
					//processBuzzJSONObject(jo);
				}
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		finally {
			MixView.dismissProgress();
		}
	}
}
