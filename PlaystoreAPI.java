package com.gocalsd.xyz;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PlaystoreAPI {

    private final String TAG = "GooglePlay API";
    private final String ERROR_TAG = "There was an error retrieving this information, please try again later...";
    private String name, packageID, logoURL, bannerURL, privacyPolicy, email, website, version, estimatedDownloads, exactDownloads, lastUpdated, launchData, developer, lastUpdateMessage, rating, noOfRatings, desc;
    private String[] images;
    private Boolean objectLoaded = false;

    public PlaystoreAPI(final Context context, final String targetedPackage){
        RequestQueue queue = Volley.newRequestQueue(context);
        String apiURL = "https://api-playstore.rajkumaar.co.in/json?id=";
        String Url = apiURL + targetedPackage;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Url, null, response -> {
                try {
                    objectLoaded = true;
                    Log.e(TAG, "Getting info for " + response.getString("packageID"));
                    Log.e(TAG, Url);

                    packageID = response.getString("packageID");
                    name = response.getString("name");
                    privacyPolicy = response.getString("privacyPolicy");
                    email = response.getString("supportEmail");
                    website = response.getString("website");
                    version = response.getString("version");
                    estimatedDownloads = response.getString("downloads");
                    exactDownloads = response.getString("downloadsExact");
                    lastUpdated = response.getString("lastUpdated");
                    launchData = response.getString("launchDate");
                    developer = response.getString("developer");
                    lastUpdateMessage = response.getString("latestUpdateMessage");
                    rating = response.getString("rating");
                    noOfRatings = response.getString("noOfUsersRated");
                    desc = response.getString("description");

                    String arrayOfStringString = response.getString("screenshots");
                    arrayOfStringString = arrayOfStringString.replace(" ","").replace("[","").replace("]","").replace("\\", "").replace("https://play-lh.googleusercontent.com/", "").replace("\"", "");
                    images = arrayOfStringString.split(",");

                    Log.e("Screenshots Downloaded", String.valueOf(images.length));

                    logoURL = response.getString("logo");
                    bannerURL = response.getString("banner");
                } catch (JSONException e) {
                    Log.d(TAG, e.toString());
                    objectLoaded = false;
                }

        }, error -> {
            objectLoaded = false;
            Log.d(TAG, "Could not find any readable data... No playstore listing?");
        });

        queue.add(jsonObjectRequest);
    }

    public String getName(){
        return name;
    }

    public URL getPolicyURL() throws MalformedURLException {
        return new URL(privacyPolicy);
    }

    public String getSupportEmail() {
        return email;
    }

    public URL getDevWebsite() throws MalformedURLException {
        return new URL(website);
    }

    public String getPackageID(){
        return packageID;
    }

    public boolean hasSessionLoaded(){
        return objectLoaded;
    }

    public String getPublishedVersion(){
        return Objects.requireNonNullElse(version, ERROR_TAG);
    }

    public int getPublishedVersionCode(){
        return Integer.parseInt(version);
    }

    public float getEstimatedDownloadsCount(){
        if(estimatedDownloads != null) {
            String formatted = estimatedDownloads.replace("+", "").replace(",", "");
            return Double.valueOf(formatted).floatValue();
        }else{
            return 0;
        }
    }

    public String getEstimatedDownloadsString() {
        return Objects.requireNonNullElse(estimatedDownloads, ERROR_TAG);
    }

    public float getExactDownloadCount(){
        if(exactDownloads != null) {
            return Double.valueOf(exactDownloads).floatValue();
        }else{
            return 0;
        }
    }

    public String getLastUpdateDate(){
        return Objects.requireNonNullElse(lastUpdated, ERROR_TAG);
    }

    public String getLaunchDate(){
        return Objects.requireNonNullElse(launchData, ERROR_TAG);
    }

    public String getPublisherName(){
        return Objects.requireNonNullElse(developer, ERROR_TAG);
    }

    public Spanned GetChangelog(){
        return HtmlCompat.fromHtml(Objects.requireNonNullElse(lastUpdateMessage, ERROR_TAG), HtmlCompat.FROM_HTML_MODE_COMPACT);
    }

    public String getCurrentRatingString(){
        return Objects.requireNonNullElse(rating, ERROR_TAG);
    }

    public float getNumberOfRatingsCount(){
        if(noOfRatings != null) {
            String formatted = noOfRatings.replace(",", "");
            return Double.valueOf(formatted).floatValue();
        }else{
            return 0;
        }
    }

    public String getNumberOfRatings(){
        return Objects.requireNonNullElse(noOfRatings, ERROR_TAG);
    }

    public Spanned getPublishedDescription(){
        return HtmlCompat.fromHtml(Objects.requireNonNullElse(desc, ERROR_TAG), HtmlCompat.FROM_HTML_MODE_COMPACT);
    }

    public List<String> getAvailableScreenshotsIds(){
        List<String> screens = new ArrayList<>();
        Collections.addAll(screens, images);
        Log.e("Screenshots Processed", String.valueOf(screens.size()));
        return screens;
    }

    public String getGoogleContentURL(){
        return "https://play-lh.googleusercontent.com/";
    }

    public URL getPublishedBannerURL() throws MalformedURLException {
        return new URL(bannerURL);
    }

    public URL getPublishedLogoURL() throws MalformedURLException {
        return new URL(logoURL);
    }

    public DecimalFormat formatValue(){
        return new DecimalFormat("#,###,###");
    }

}
