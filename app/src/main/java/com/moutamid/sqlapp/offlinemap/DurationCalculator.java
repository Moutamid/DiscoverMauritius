package com.moutamid.sqlapp.offlinemap;

public class DurationCalculator {

    private static final double WALKING_SPEED_KM_PER_HOUR = 5.0;
    private static final double BIKING_SPEED_KM_PER_HOUR = 15.0;
    private static final double DRIVING_SPEED_KM_PER_HOUR = 51.0;
    private static final double TRAM_SPEED_KM_PER_HOUR = 42.0;

    public static double calculateWalkingDuration(double distanceKm) {
        return distanceKm / WALKING_SPEED_KM_PER_HOUR;
    }

    public static double calculateBikingDuration(double distanceKm) {
        return distanceKm / BIKING_SPEED_KM_PER_HOUR;
    }

    public static double calculateDrivingDuration(double distanceKm) {
        return distanceKm / DRIVING_SPEED_KM_PER_HOUR;
    }
    public static double calculateTramDuration(double distanceKm) {
        return distanceKm / TRAM_SPEED_KM_PER_HOUR;
    }

    public static String formatDuration(double durationHours) {
        int hours = (int) durationHours;
        int minutes = (int) ((durationHours - hours) * 60);
        if (hours == 0) {
            return String.format("%d min", minutes);


        }
        else
        {
            return String.format("%d hr %d min", hours, minutes);

        }

    }
}
