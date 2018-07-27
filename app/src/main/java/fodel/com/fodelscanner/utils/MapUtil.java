package fodel.com.fodelscanner.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import fodel.com.fodelscanner.R;

/**
 * use to google map
 */
public class MapUtil {

    public static class DistanceUtil {

        public static double distance(double lat1, double lon1, double lat2,
                                      double lon2) {

            if ((lat1 == lat2) && (lon1 == lon2)) {
                return 0;
            } else
                return distance(lat1, lon1, lat2, lon2, 'K');
        }

        public static double distance(double lat1, double lon1, double lat2,
                                      double lon2, char unit) {
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                    + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            if (unit == 'K') {
                dist = dist * 1.609344;
            } else if (unit == 'N') {
                dist = dist * 0.8684;
            }
            return (dist);
        }

        private static double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        private static double rad2deg(double rad) {
            return (rad * 180.0 / Math.PI);
        }

        public static String convertedDistance(double instance) {
            if (instance < 1) {
                return Math.round(instance * 1000) + " M";
            }
            return Math.round(instance) + " KM";
        }
    }


    public static boolean startNavigation(Context context, LatLng start, LatLng end) {
        if (!NetUtils.isConnected(context)) {
            ToastUtils.showShort(context, context.getString(R.string.no_network));
            return false;
        }
        if (start != null || end != null) {
            if (AppUtils.isAvailable(context, "com.google.android.apps.maps")) {
                String saddr = getAddress(context, start.latitude, start.longitude);
                String daddr = getAddress(context, end.latitude, end.longitude);
                if (StringUtils.isEmpty(saddr) || StringUtils.isEmpty(daddr)) {
                    ToastUtils.showShort(context, "get the location fail");
                    return false;
                }
                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?" + "saddr=" + saddr + "&daddr=" + daddr);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
                return true;
            }
        }
        return false;
    }

    public static String getAddress(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> address = geocoder.getFromLocation(lat, lng, 1);
            if (address != null && address.size() >= 1) {
                Address addr = address.get(0);
                return addr.getAddressLine(0);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static void showLocation(Context context, double lt, double lg, String address) {
        if (AppUtils.isAvailable(context, "com.google.android.apps.maps")) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?z=12&q=" + lg + "," + lt + "(" + address + ")");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }
}
