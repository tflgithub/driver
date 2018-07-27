package fodel.com.fodelscanner.helper;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.upgrade.HttpUtils;
import fodel.com.fodelscanner.utils.BitmapFile;
import fodel.com.fodelscanner.utils.LogUtils;
/**
 * Created by fula on 2017/8/2.
 */

public class MapControllerHelper extends MapController {

    GoogleApiClient googleApiClient;

    Context context;

    Polyline polyline;

    Marker cameraPositionMarker;

    public MapControllerHelper(MapFragment mapFragment, MapControllerReady callback) {
        super(mapFragment, callback);
        context = mapFragment.getActivity();
    }

    public void moveToCameraPosition(LatLng latLng) {
        if (cameraPositionMarker != null) {
            setCameraPositionMarker(true);
            cameraPositionMarker.setPosition(latLng);
        } else {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFile.bitmapFromVectorDrawable(context, R.drawable.ic_location)));
            cameraPositionMarker = getMap().addMarker(markerOptions);
        }
    }

    public void setCameraPositionMarker(boolean isVisible) {
        if (cameraPositionMarker != null) {
            cameraPositionMarker.setVisible(isVisible);
        }
    }

    public Projection.Pixel toScreenLocation(LatLng latLng) {
        return new Projection(getMap().getCameraPosition().zoom).fromCoordinatesToPixel(latLng);
    }

    public void startTrackMyLocation(final GoogleMap map, final long interval, final MapController.ChangeMyLocation callback) {
        GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            public void onConnected(Bundle bundle) {
                Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if(location!=null) {
                    animateTo(location.getLatitude(), location.getLongitude(), 16);
                    if (callback != null) {
                        callback.changed(map, location, true);
                    }
                }
                LocationRequest request = LocationRequest.create().setInterval(interval).setPriority(100);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, new LocationListener() {
                    public void onLocationChanged(Location location) {
                        if (map != null) {
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            map.setMyLocationEnabled(true);
                        }
                        if (callback != null) {
                            callback.changed(map, location, false);
                        }
                    }
                });
            }

            public void onConnectionSuspended(int i) {
            }
        };
        if (this.googleApiClient == null) {
            this.googleApiClient = (new GoogleApiClient.Builder(context)).addConnectionCallbacks(connectionCallbacks).addApi(LocationServices.API).build();
        }
        this.googleApiClient.registerConnectionCallbacks(connectionCallbacks);
        this.googleApiClient.connect();
    }

    public void stopTrackMyLocation() {
        if (this.googleApiClient != null && this.googleApiClient.isConnected()) {
            this.googleApiClient.disconnect();
        }
    }

    public void route(LatLng origin, LatLng dest) {
        clearPolyline();
        String url = getUrl(origin, dest);
        // Start downloading json data from Google Directions API
        new FetchUrl().execute(url);
    }

    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        //test https://maps.googleapis.com/maps/api/directions/json?origin=25.18462471150365,55.33328868448734&destination=25.2229718,55.3229607&sensor=false
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = HttpUtils.get(url[0]);
                LogUtils.d("Background Task data", data.toString());
            } catch (Exception e) {
                LogUtils.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            this.dialog = new ProgressDialog(context);
            this.dialog.setMessage("Path planning....");
            this.dialog.setCancelable(true);
            this.dialog.show();
            super.onPreExecute();
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                LogUtils.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                LogUtils.d("ParserTask", parser.toString());
                // Starts parsing data
                routes = parser.parse(jObject);
                LogUtils.d("ParserTask", "Executing routes");
                LogUtils.d("ParserTask", routes.toString());

            } catch (Exception e) {
                LogUtils.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            ArrayList<LatLng> points;
            // Traversing through all the routes
            PolylineOptions lineOptions = null;
            if (result == null) {
                return;
            }
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
                LogUtils.d("onPostExecute", "onPostExecute lineoptions decoded");
            }
            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                polyline = getMap().addPolyline(lineOptions);
            } else {
                LogUtils.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    public void clearPolyline() {
        if (polyline != null) {
            polyline.remove();
        }
    }

    public void clearCameraMarker() {
        cameraPositionMarker = null;
    }

    public void clearOverlay() {
        clearPolyline();
        clearCameraMarker();
    }
}
