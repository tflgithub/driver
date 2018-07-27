package fodel.com.fodelscanner.helper;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by fula on 2017/8/3.
 */

public class Projection {

    private double PixelTileSize = 256d;
    private double RadiansToDegreesRatio = Math.PI / 180d;
    private Pixel PixelGlobeCenter;
    private double XPixelsToDegreesRatio;
    private double YPixelsToRadiansRatio;
    /**
     * Constructor
     *
     * @param zoomLevel
     */
    public Projection(float zoomLevel) {
        double pixelGlobeSize = this.PixelTileSize * Math.pow(2d, zoomLevel);
        this.XPixelsToDegreesRatio = pixelGlobeSize / 360d;
        this.YPixelsToRadiansRatio = pixelGlobeSize / (2d * Math.PI);
        double halfPixelGlobeSize = pixelGlobeSize / 2d;
        this.PixelGlobeCenter = new Pixel(halfPixelGlobeSize, halfPixelGlobeSize);
    }

    /**
     * Translates from world Coordinate to flat projected Pixel
     *
     * @param coordinates
     * @return
     */
    public Pixel fromCoordinatesToPixel(LatLng coordinates) {
        double x = Math.round(this.PixelGlobeCenter.x + (coordinates.longitude * this.XPixelsToDegreesRatio));
        double f = Math.min(Math.max(Math.sin(coordinates.latitude * RadiansToDegreesRatio), -0.9999d), 0.9999d);
        double y = Math.round(this.PixelGlobeCenter.y + .5d * Math.log((1d + f) / (1d - f)) * -this.YPixelsToRadiansRatio);
        return new Pixel(x, y);
    }

    public class Pixel {
        public double x;
        public double y;

        public Pixel(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
