package com.vericakaranakova.advocacyapp;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OfficialsDownloader {

    private static final String TAG = "OfficialsDownloader";

    public static String loc = "";

    public static MainActivity mainActivity;
    public static String address;
    public static RequestQueue queue;
    public static ArrayList<Officials> officials = new ArrayList<>();

    public static final String apiKey = "AIzaSyD3Nm7XsEU5xo2bPM0sCQISe3ZrNn9q9FQ";
    public static final String url = "https://www.googleapis.com/civicinfo/v2/representatives";

    public static void downloadOfficials(MainActivity mainActivityIn, String addr) {
        mainActivity = mainActivityIn;
        address = addr;
        queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(url).buildUpon();
        buildURL.appendQueryParameter("key", apiKey);
        buildURL.appendQueryParameter("address", address);
        String urlToUse = buildURL.build().toString();

        Log.d(TAG, "downloadOfficials: " + urlToUse);

        Response.Listener<JSONObject> listener =
                response -> parseJSON(response.toString());

        Response.ErrorListener error =
                error1 -> mainActivity.updateData("", null);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public static void parseJSON(String s){
        try {
            //normalizedInput
            JSONObject sObj = new JSONObject(s);
            JSONObject jObjMain = sObj.getJSONObject("normalizedInput");
            if (jObjMain.has("line1")) {
                loc += jObjMain.getString("line1");
            }

            if (jObjMain.has("city")) {
                if (!loc.equals("")) {
                    loc += ", ";
                }
                loc += jObjMain.getString("city");
            }

            if (jObjMain.has("state")) {
                if (!loc.equals("")) {
                    loc += ", ";
                }
                loc += jObjMain.getString("state");
            }

            if (jObjMain.has("zip")) {
                if (!loc.equals("")) {
                    loc += " ";
                }
                loc += jObjMain.getString("zip");
            }

            // Get officials
            JSONArray jsonArray = sObj.getJSONArray("offices");
            if (jsonArray != null){
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String position = jsonObject.getString("name");
                    JSONArray idxArray = jsonObject.getJSONArray("officialIndices");
                    if (idxArray != null) {
                        for (int j = 0; j < idxArray.length(); j++) {
                            int idx = (Integer) idxArray.get(j);
                            JSONArray officialsArray = sObj.getJSONArray("officials");
                            JSONObject official = officialsArray.getJSONObject(idx);
                            String name = official.getString("name");

                            String adrLine = "";
                            String officialCity = "null";
                            String officialState = "null";
                            String officialZip = "null";
                            try {
                                JSONArray adrArray = official.getJSONArray("address");
                                JSONObject adr = adrArray.getJSONObject(0);
                                try {
                                    adrLine += adr.getString("line1");
                                } catch (Exception e) {
                                    adrLine += "";
                                }
                                try {
                                    if (!adrLine.equals("")) {
                                        adrLine += (", " + adr.getString("line2"));
                                    } else {
                                        adrLine += adr.getString("line2");
                                    }
                                } catch (Exception e) {
                                    adrLine += "";
                                }
                                try {
                                    if (!adrLine.equals("")) {
                                        adrLine += (", " + adr.getString("line3"));
                                    } else {
                                        adrLine += adr.getString("line3");
                                    }
                                } catch (Exception e) {
                                    adrLine += "";
                                }

                                officialCity = adr.getString("city");
                                officialState = adr.getString("state");
                                officialZip = adr.getString("zip");

                            } catch (Exception e) {
                                adrLine += "";
                            }

                            String party = "unknown";
                            try {
                                party = official.getString("party");
                            } catch (Exception e) {
                                party = "unknown";
                            }

                            String phone = "";
                            try {
                                phone = (String) official.getJSONArray("phones").get(0);
                            } catch (Exception e) {
                                phone = "null";
                            }

                            String officialURL = "";
                            try {
                                officialURL = (String) official.getJSONArray("urls").get(0);
                            } catch (Exception e) {
                                officialURL = "null";
                            }

                            String officialEmail = "";
                            try {
                                officialEmail = (String) official.getJSONArray("emails").get(0);
                            } catch (Exception e) {
                                officialEmail = "null";
                            }

                            String photoURL = "missing";
                            try {
                                photoURL = official.getString("photoUrl");
                                if(!photoURL.contains("https"))
                                    photoURL = photoURL.replaceAll("http","https").trim();
                            } catch (Exception e) {
                                photoURL = "missing";
                            }

                            String facebook = "null";
                            String youtube = "null";
                            String twitter = "null";
                            try {
                                for (int m = 0; m < official.getJSONArray("channels").length(); m++) {
                                    JSONObject channel = official.getJSONArray("channels").getJSONObject(m);
                                    String type = channel.getString("type");
                                    if (type.equals("Facebook")) {
                                        facebook = channel.getString("id");
                                    } else if (type.equals("YouTube")) {
                                        youtube = channel.getString("id");
                                    } else if (type.equals("Twitter")) {
                                        twitter = channel.getString("id");
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            officials.add(new Officials(loc, position, name, adrLine, officialCity, officialState, officialZip, party, phone, officialEmail, photoURL, officialURL, facebook, youtube, twitter));
                        }
                    }
                }
            }
            MainActivity.updateData(loc, officials);
            loc = "";
            officials.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLoc() {
        return loc;
    }
}
