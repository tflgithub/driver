package fodel.com.fodelscanner.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/19.
 */

public class MapController {

    private static final String TAG = MapController.class.getSimpleName();
    private static Context context;
    private GoogleMap map;
    private ArrayList<Marker> markers;
    private GoogleMap.OnCameraChangeListener ccListener;
    private GoogleApiClient googleApiClient;

    /**
     * attach Google Maps
     *
     * @param map
     */
    public MapController(GoogleMap map) {
        if (map == null) {
            Log.e(TAG, "GoogleMap can't not be null.");

            throw new RuntimeException("GoogleMap can't not be null.");
        }

        this.map = map;
    }

    public MapController(MapView mapView, final MapControllerReady callback) {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapController.this.map = googleMap;

                if (callback != null) {
                    callback.already(MapController.this);
                }
            }
        });
    }

    public MapController(MapFragment mapFragment, final MapControllerReady callback) {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapController.this.map = googleMap;

                if (callback != null) {
                    callback.already(MapController.this);
                }
            }
        });
    }

    private MapController() {
    }

    /**
     * initialize Google Maps
     *
     * @param context
     * @throws GooglePlayServicesNotAvailableException
     */
    public static void initialize(Context context)
            throws GooglePlayServicesNotAvailableException {
        MapController.context = context;

        MapsInitializer.initialize(context);
    }

    /**
     * return map's instance
     *
     * @return
     */
    public GoogleMap getMap() {
        return map;
    }

    /**
     * return the type of map that's currently displayed.
     *
     * @return
     */
    public MapType getType() {
        return MapType.valueOf(String.valueOf(map.getMapType()));
    }

    /**
     * sets the type of map tiles that should be displayed
     *
     * @param type
     */
    public void setType(MapType type) {
        map.setMapType(type.ordinal());
    }





    /**
     * stop tracking my current location
     */
    public void stopTrackMyLocation() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }




    /**
     * show my current location
     */
    public void showMyLocation() {

       // map.setMyLocationEnabled(true);
    }

    /**
     * animate to specific location
     *
     * @param latLng
     * @param zoom
     * @param callback
     */
    public void animateTo(LatLng latLng, int zoom, final ChangePosition callback) {
        if (ccListener == null) {
            ccListener = new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition position) {
                    map.setOnCameraChangeListener(null);

                    ccListener = null;

                    if (callback != null) {
                        callback.changed(map, position);
                    }
                }
            };

            map.setOnCameraChangeListener(ccListener);
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /**
     * animate to specific location
     *
     * @param latLng
     */
    public void animateTo(LatLng latLng) {
        animateTo(latLng, (int) map.getCameraPosition().zoom, null);
    }

    /**
     * animate to specific location
     *
     * @param latLng
     * @param callback
     */
    public void animateTo(LatLng latLng, ChangePosition callback) {
        animateTo(latLng, (int) map.getCameraPosition().zoom, callback);
    }

    /**
     * animate to specific location
     *
     * @param lat
     * @param lng
     * @param callback
     */
    public void animateTo(double lat, double lng, ChangePosition callback) {
        animateTo(new LatLng(lat, lng), (int) map.getCameraPosition().zoom,
                callback);
    }

    /**
     * animate to specific location
     *
     * @param lat
     * @param lng
     */
    public void animateTo(double lat, double lng) {
        animateTo(new LatLng(lat, lng), (int) map.getCameraPosition().zoom,
                null);
    }

    /**
     * animate and zoom to specific location
     *
     * @param latLng
     * @param zoom
     */
    public void animateTo(LatLng latLng, int zoom) {
        animateTo(latLng, zoom, null);
    }

    /**
     * animate and zoom to specific location
     *
     * @param lat
     * @param lng
     * @param zoom
     */
    public void animateTo(double lat, double lng, int zoom) {
        animateTo(new LatLng(lat, lng), zoom, null);
    }

    /**
     * animate and zoom to specific location
     *
     * @param lat
     * @param lng
     * @param zoom
     * @param callback
     */
    public void animateTo(double lat, double lng, int zoom,
                          ChangePosition callback) {
        animateTo(new LatLng(lat, lng), zoom, callback);
    }

    /**
     * move to specific location
     *
     * @param latLng
     * @param callback
     */
    public void moveTo(LatLng latLng, int zoom, final ChangePosition callback) {
        if (ccListener == null) {
            ccListener = new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition position) {
                    map.setOnCameraChangeListener(null);

                    ccListener = null;

                    if (callback != null) {
                        callback.changed(map, position);
                    }
                }
            };

            map.setOnCameraChangeListener(ccListener);
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /**
     * move to specific location
     *
     * @param latLng
     */
    public void moveTo(LatLng latLng) {
        moveTo(latLng, (int) map.getCameraPosition().zoom, null);
    }

    /**
     * move to specific location
     *
     * @param latLng
     * @param callback
     */
    public void moveTo(LatLng latLng, ChangePosition callback) {
        moveTo(latLng, (int) map.getCameraPosition().zoom, callback);
    }

    /**
     * move to specific location
     *
     * @param lat
     * @param lng
     * @param callback
     */
    public void moveTo(double lat, double lng, ChangePosition callback) {
        moveTo(new LatLng(lat, lng), (int) map.getCameraPosition().zoom,
                callback);
    }

    /**
     * move to specific location
     *
     * @param lat
     * @param lng
     */
    public void moveTo(double lat, double lng) {
        moveTo(new LatLng(lat, lng), (int) map.getCameraPosition().zoom, null);
    }

    /**
     * move and zoom to specific location
     *
     * @param latLng
     * @param zoom
     */
    public void moveTo(LatLng latLng, int zoom) {
        moveTo(latLng, zoom, null);
    }

    /**
     * move and zoom to specific location
     *
     * @param lat
     * @param lng
     * @param zoom
     */
    public void moveTo(double lat, double lng, int zoom) {
        moveTo(new LatLng(lat, lng), zoom, null);
    }

    /**
     * move and zoom to specific location
     *
     * @param lat
     * @param lng
     * @param zoom
     * @param callback
     */
    public void moveTo(double lat, double lng, int zoom, ChangePosition callback) {
        moveTo(new LatLng(lat, lng), zoom, callback);
    }

    /**
     * @param southwest
     * @param northeast
     * @param padding
     * @param smooth
     * @param callback
     */
    public void setBounds(LatLng southwest, LatLng northeast, int padding,
                          boolean smooth, final ChangePosition callback) {
        if (ccListener == null) {
            ccListener = new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition position) {
                    map.setOnCameraChangeListener(null);

                    ccListener = null;

                    if (callback != null) {
                        callback.changed(map, position);
                    }
                }
            };

            map.setOnCameraChangeListener(ccListener);
        }

        if (smooth) {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(
                    new LatLngBounds(southwest, northeast), padding));
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(
                    new LatLngBounds(southwest, northeast), padding));
        }
    }

    /**
     * @param southwest
     * @param northeast
     * @param padding
     * @param smooth
     */
    public void setBounds(LatLng southwest, LatLng northeast, int padding,
                          boolean smooth) {
        setBounds(southwest, northeast, padding, smooth, null);
    }

    /**
     * @param southwest
     * @param northeast
     * @param padding
     */
    public void setBounds(LatLng southwest, LatLng northeast, int padding) {
        setBounds(southwest, northeast, padding, true, null);
    }

    /**
     * @param swLat
     * @param swLng
     * @param neLat
     * @param neLng
     * @param padding
     * @param smooth
     */
    public void setBounds(double swLat, double swLng, double neLat,
                          double neLng, int padding, boolean smooth) {
        setBounds(new LatLng(swLat, swLng), new LatLng(neLat, neLng), padding,
                smooth, null);
    }

    /**
     * @param swLat
     * @param swLng
     * @param neLat
     * @param neLng
     * @param padding
     */
    public void setBounds(double swLat, double swLng, double neLat,
                          double neLng, int padding) {
        setBounds(new LatLng(swLat, swLng), new LatLng(neLat, neLng), padding,
                true, null);
    }

    /**
     * @param swLat
     * @param swLng
     * @param neLat
     * @param neLng
     * @param padding
     * @param callback
     */
    public void setBounds(double swLat, double swLng, double neLat,
                          double neLng, int padding, ChangePosition callback) {
        setBounds(new LatLng(swLat, swLng), new LatLng(neLat, neLng), padding,
                true, callback);
    }

    /**
     * zoom map
     *
     * @param zoom
     * @param smooth
     * @param callback
     */
    public void zoomTo(int zoom, boolean smooth, ChangePosition callback) {
        if (smooth) {
            animateTo(map.getCameraPosition().target, zoom, callback);
        } else {
            moveTo(map.getCameraPosition().target, zoom, callback);
        }
    }

    /**
     * zoom map
     *
     * @param zoom
     */
    public void zoomTo(int zoom) {
        zoomTo(zoom, true, null);
    }

    /**
     * zoom map
     *
     * @param zoom
     * @param callback
     */
    public void zoomTo(int zoom, ChangePosition callback) {
        zoomTo(zoom, true, callback);
    }

    /**
     * zoom map
     *
     * @param zoom
     * @param smooth
     */
    public void zoomTo(int zoom, boolean smooth) {
        zoomTo(zoom, smooth, null);
    }

    /**
     * zoom map in
     */
    public void zoomIn() {
        zoomTo((int) (map.getCameraPosition().zoom + 1), true, null);
    }

    /**
     * zoom map in
     *
     * @param callback
     */
    public void zoomIn(ChangePosition callback) {
        zoomTo((int) (map.getCameraPosition().zoom + 1), true, callback);
    }

    /**
     * zoom map in
     *
     * @param smooth
     * @param callback
     */
    public void zoomIn(boolean smooth, ChangePosition callback) {
        zoomTo((int) (map.getCameraPosition().zoom + 1), smooth, callback);
    }

    /**
     * zoom map out
     */
    public void zoomOut() {
        zoomTo((int) (map.getCameraPosition().zoom - 1), true, null);
    }

    /**
     * zoom map out
     *
     * @param callback
     */
    public void zoomOut(ChangePosition callback) {
        zoomTo((int) (map.getCameraPosition().zoom - 1), true, callback);
    }

    /**
     * zoom map out
     *
     * @param smooth
     * @param callback
     */
    public void zoomOut(boolean smooth, ChangePosition callback) {
        zoomTo((int) (map.getCameraPosition().zoom - 1), smooth, callback);
    }


    @Deprecated
    public void setInfoWindow(final View v) {
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }


    @Deprecated
    public void setInfoContents(final View v) {
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return v;
            }
        });
    }

    /**
     * set the info-window adpater
     *
     * @param adapter
     */
    public void setInfoWindowAdapter(GoogleMap.InfoWindowAdapter adapter) {
        map.setInfoWindowAdapter(adapter);
    }

    /**
     * when map is clicked
     *
     * @param callback
     */
    public void whenMapClick(final ClickCallback callback) {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                callback.clicked(map, latLng);
            }
        });
    }

    /**
     * when map is long clicked
     *
     * @param callback
     */
    public void whenMapLongClick(final ClickCallback callback) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                callback.clicked(map, latLng);
            }
        });
    }

    /**
     * when info window is clicked
     *
     * @param callback
     */
    public void whenInfoWindowClick(final MarkerCallback callback) {
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                callback.invokedMarker(map, marker);
            }
        });
    }

    /**
     * when marker is clicked
     *
     * @param callback
     */
    public void whenMarkerClick(final MarkerCallback callback) {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                callback.invokedMarker(map, marker);

                return true;
            }
        });
    }

    /**
     * when marker is dragged
     *
     * @param callback
     */
    public void whenMarkerDrag(final MarkerDrag callback) {
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                callback.markerDragStart(map, marker);
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                callback.markerDrag(map, marker);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                callback.markerDragEnd(map, marker);
            }
        });
    }

    /**
     * add marker to map
     *
     * @param opts
     * @param callback
     * @return
     */
    public Marker addMarker(MarkerOptions opts, MarkerCallback callback) {
        Marker marker = map.addMarker(opts);

        if (markers == null) {
            markers = new ArrayList<Marker>();
        }

        markers.add(marker);

        if (callback != null) {
            callback.invokedMarker(map, marker);
        }

        return marker;
    }

    /**
     * add marker to map
     *
     * @param opts
     * @return
     */
    public Marker addMarker(MarkerOptions opts) {
        return addMarker(opts, null);
    }

    /**
     * add marker to map
     *
     * @param latLng
     * @param opts
     * @return
     */
    public Marker addMarker(LatLng latLng, MarkerOptions opts) {
        return addMarker(opts.position(latLng), null);
    }

    /**
     * add marker to map
     *
     * @param lat
     * @param lng
     * @param opts
     * @return
     */
    public Marker addMarker(double lat, double lng, MarkerOptions opts) {
        return addMarker(opts.position(new LatLng(lat, lng)), null);
    }

    /**
     * add marker to map
     *
     * @param latLng
     * @return
     */
    public Marker addMarker(LatLng latLng) {
        return addMarker(new MarkerOptions().position(latLng), null);
    }

    /**
     * add marker to map
     *
     * @param lat
     * @param lng
     * @return
     */
    public Marker addMarker(double lat, double lng) {
        return addMarker(new MarkerOptions().position(new LatLng(lat, lng)),
                null);
    }

    /**
     * add marker to map
     *
     * @param latLng
     * @param opts
     * @param callback
     * @return
     */
    public Marker addMarker(LatLng latLng, MarkerOptions opts,
                            MarkerCallback callback) {
        return addMarker(opts.position(latLng), callback);
    }

    /**
     * add marker to map
     *
     * @param lat
     * @param lng
     * @param opts
     * @param callback
     * @return
     */
    public Marker addMarker(double lat, double lng, MarkerOptions opts,
                            MarkerCallback callback) {
        return addMarker(opts.position(new LatLng(lat, lng)), callback);
    }

    /**
     * add marker to map
     *
     * @param latLng
     * @param callback
     * @return
     */
    public Marker addMarker(LatLng latLng, MarkerCallback callback) {
        return addMarker(new MarkerOptions().position(latLng), callback);
    }

    /**
     * add marker to map
     *
     * @param lat
     * @param lng
     * @param callback
     * @return
     */
    public Marker addMarker(double lat, double lng, MarkerCallback callback) {
        return addMarker(new MarkerOptions().position(new LatLng(lat, lng)),
                callback);
    }

    /**
     * add all markers to map
     *
     * @param allOpts
     * @param callback
     */
    public void addMarkers(ArrayList<MarkerOptions> allOpts,
                           MarkerCallback callback) {
        if (markers == null) {
            markers = new ArrayList<Marker>();
        }

        for (MarkerOptions opts : allOpts) {
            Marker marker = map.addMarker(opts);

            markers.add(marker);

            if (callback != null) {
                callback.invokedMarker(map, marker);
            }
        }
    }

    /**
     * add all markers to map
     *
     * @param allOpts
     */
    public void addMarkers(ArrayList<MarkerOptions> allOpts) {
        addMarkers(allOpts, null);
    }

    /**
     * return all markers
     *
     * @return
     */
    public ArrayList<Marker> getMarkers() {
        return markers;
    }

    /**
     * return specific marker
     *
     * @param index
     * @return
     */
    public Marker getMarker(int index) {
        return markers.get(index);
    }

    /**
     * clear all markers
     */
    public void clearMarkers() {
        map.clear();

        if (markers != null) {
            markers.clear();
        }
    }

    /**
     * show traffic layer
     *
     * @param enabled
     */
    public void showTraffic(boolean enabled) {
        map.setTrafficEnabled(enabled);
    }

    /**
     * show indoor layer
     *
     * @param enabled
     */
    public void showIndoor(boolean enabled) {
        map.setIndoorEnabled(enabled);
    }

    /**
     * find specific location
     *
     * @param location
     * @param callback
     */
    public void find(String location, FindResult callback) {
        Geocoder geocoder = new Geocoder(context);
        ArrayList<Address> addresses = new ArrayList<Address>();

        try {
            addresses = (ArrayList<Address>) geocoder.getFromLocationName(
                    location, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }

        findCallback(callback, addresses);
    }

    /**
     * find specific location
     *
     * @param location
     */
    public void find(String location) {
        find(location, null);
    }

    /**
     * find specific location
     *
     * @param location
     * @param callback
     */
    public void findAsync(final String location, final FindResult callback) {
        new AsyncTask<Void, Void, Void>() {
            private ProgressDialog dialog;
            private ArrayList<Address> addresses;

            @Override
            protected void onPreExecute() {
                dialog = new ProgressDialog(context);

                dialog.setMessage("Loading...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                Geocoder geocoder = new Geocoder(context);

                try {
                    addresses = (ArrayList<Address>) geocoder
                            .getFromLocationName(location, 5);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                findCallback(callback, addresses);
            }
        }.execute();
    }

    /**
     * find specific location
     *
     * @param location
     */
    public void findAsync(String location) {
        findAsync(location, null);
    }

    private void findCallback(FindResult callback, ArrayList<Address> addresses) {
        if (callback != null) {
            callback.found(map, addresses);
        } else {
            for (Address address : addresses) {
                MarkerOptions opts = new MarkerOptions();
                LatLng latLng = new LatLng(address.getLatitude(),
                        address.getLongitude());

                opts.position(latLng);
                opts.title(address.toString());
                opts.snippet(latLng.toString());

                addMarker(opts);
            }

            animateTo(new LatLng(addresses.get(0).getLatitude(), addresses.get(
                    0).getLongitude()));
        }
    }

    public enum MapType {
        MAP_TYPE_NONE, MAP_TYPE_NORMAL, MAP_TYPE_SATELLITE, MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID
    }

    public enum TrackType {
        TRACK_TYPE_MOVE, TRACK_TYPE_ANIMATE, TRACK_TYPE_NONE
    }

    public interface MapControllerReady {
        public void already(MapController controller);
    }

    public interface ChangeMyLocation {
        public void changed(GoogleMap map, Location location,
                            boolean lastLocation);
    }

    public interface ChangePosition {
        public void changed(GoogleMap map, CameraPosition position);
    }

    public interface ClickCallback {
        public void clicked(GoogleMap map, LatLng latLng);
    }

    public interface MarkerCallback {
        public void invokedMarker(GoogleMap map, Marker marker);
    }

    public interface MarkerDrag {
        public void markerDragStart(GoogleMap map, Marker marker);

        public void markerDrag(GoogleMap map, Marker marker);

        public void markerDragEnd(GoogleMap map, Marker marker);
    }

    public interface FindResult {
        public void found(GoogleMap map, ArrayList<Address> addresses);
    }
}