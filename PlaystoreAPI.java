package com.project.packageName;

import android.content.Context;
import android.text.Spanned;
import android.util.Log;

import androidx.core.text.HtmlCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class PlaystoreAPI {
  
    private String TAG = "GooglePlay API";
    private String version, estimatedDownloads, exactDownloads, lastUpdated,launchData,developer,lastUpdateMessage,rating,noOfRatings,desc;
    private String[] screenshots;
    private String logoURL, bannerURL;

    private boolean hasListing;

    private final String apiURL = "https://api-playstore.rajkumaar.co.in/json?id=";

    public PlaystoreAPI(final Context context, final String targetedPackage){
        RequestQueue queue = Volley.newRequestQueue(context);
        String Url = apiURL + targetedPackage;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Url, null, response -> {
            try {
                Log.e(TAG, "Getting info for " + response.getString("packageID"));
                Log.e(TAG, Url);

                version = response.getString("version");
                estimatedDownloads = response.getString("downloads");
                exactDownloads = response.getString("downloadsExact");
                lastUpdated = response.getString("lastUpdated");
                launchData = response.getString("launchDate");
                developer = response.getString("developer");
                lastUpdateMessage = response.getString("latest_update_message");
                rating = response.getString("rating");
                noOfRatings = response.getString("noOfUsersRated");
                desc = response.getString("description");

                screenshots = new String[]{response.getString("screenshots")};

                logoURL = response.getString("logo");
                bannerURL = response.getString("banner");
                hasListing = true;
            } catch (JSONException e) {
                hasListing = false;
                Log.d(TAG, e.toString());
            }
        }, error -> {
            hasListing = false;
            Log.d(TAG, "Could not find any readable data... No playstore listing?");
        });

        queue.add(jsonObjectRequest);
    }

    public String getPublishedVersion(){
        if(!version.isBlank()){
            return version;
        }
        return "Unknown";
    }

    public float getEstimatedDownloadsCount(){
        String formatted = estimatedDownloads.replace("+", "").replace(",", "");
        return Double.valueOf(formatted).floatValue();
    }

    public String getEstimatedDownloadsString(){
        return estimatedDownloads;
    }

    public float getExactDownloadCount(){
        return Double.valueOf(exactDownloads).floatValue();
    }

    public String getLastUpdateDate(){
        return lastUpdated;
    }

    public String getLaunchDate(){
        return launchData;
    }

    public String getPublisherName(){
        return developer;
    }

    public Spanned GetChangelog(){
        return HtmlCompat.fromHtml(lastUpdateMessage, HtmlCompat.FROM_HTML_MODE_COMPACT);
    }

    public String getCurrentRatingString(){
        return rating;
    }

    public float getNumberOfRatingsCount(){
        String formatted = noOfRatings.replace(",", "");
        return Double.valueOf(formatted).floatValue();
    }

    public String getNumberOfRatings(){
        return noOfRatings;
    }

    public Spanned getPublishedDescription(){
        return HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_COMPACT);
    }

    public String[] getAvailableScreenshots(){
        return screenshots;
    }

    public URL getPublishedBannerURL() throws MalformedURLException {
        return new URL(bannerURL);
    }

    public URL getPublishedLogoURL() throws MalformedURLException {
        return new URL(logoURL);
    }

    public boolean isListingAvailable(){
        return hasListing;
    }

    public DecimalFormat formatValue(){
        return new DecimalFormat("#,###,###");
    }

}
