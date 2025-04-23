package com.moutamid.sqlapp.offlinemap;
public class DistanceCalculator {

    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    public static double calculateDistance(double startLat, double startLng, double endLat, double endLng) {
        double dLat = Math.toRadians(endLat - startLat);
        double dLng = Math.toRadians(endLng - startLng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // Distance in KM
    }

    public static double convertMetersToKilometers(double meters) {
        return meters / 1000;
    }
}
