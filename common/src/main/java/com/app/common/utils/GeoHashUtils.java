package com.app.common.utils;

public class GeoHashUtils {

    private static final char[] BASE_32 = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private static final int[] BITS = {16, 8, 4, 2, 1};

    /**
     * Encodes the given latitude and longitude into a geohash
     *
     * @param latitude  Latitude to encode
     * @param longitude Longitude to encode
     * @return Geohash encoding of the longitude and latitude
     */
    public static String encode(double latitude, double longitude, int precision) {
//        double[] latInterval = {-90.0, 90.0};
//        double[] lngInterval = {-180.0, 180.0};
        double latInterval0 = -90.0;
        double latInterval1 = 90.0;
        double lngInterval0 = -180.0;
        double lngInterval1 = 180.0;

        final StringBuilder geohash = new StringBuilder();
        boolean isEven = true;

        int bit = 0;
        int ch = 0;

        while (geohash.length() < precision) {
            double mid = 0.0;
            if (isEven) {
//                mid = (lngInterval[0] + lngInterval[1]) / 2D;
                mid = (lngInterval0 + lngInterval1) / 2D;
                if (longitude > mid) {
                    ch |= BITS[bit];
//                    lngInterval[0] = mid;
                    lngInterval0 = mid;
                } else {
//                    lngInterval[1] = mid;
                    lngInterval1 = mid;
                }
            } else {
//                mid = (latInterval[0] + latInterval[1]) / 2D;
                mid = (latInterval0 + latInterval1) / 2D;
                if (latitude > mid) {
                    ch |= BITS[bit];
//                    latInterval[0] = mid;
                    latInterval0 = mid;
                } else {
//                    latInterval[1] = mid;
                    latInterval1 = mid;
                }
            }

            isEven = !isEven;

            if (bit < 4) {
                bit++;
            } else {
                geohash.append(BASE_32[ch]);
                bit = 0;
                ch = 0;
            }
        }

        return geohash.toString();
    }
}
