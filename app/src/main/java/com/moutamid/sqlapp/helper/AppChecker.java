package com.moutamid.sqlapp.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class AppChecker {

    private static final String PREFS_NAME = "AppCheckerPrefs";
    private static final String LAST_CHECK_DATE = "lastCheckDate";
    private static final String APP_ENABLED = "appEnabled";

    public static void checkApp(Activity activity) {
        String appName = "My Trips2";

        // Check if the app is currently enabled
        if (!isAppEnabled(activity)) {
            showAppDisabledDialog(activity);
            return;
        }

        // Check if it's time to check again
        if (!shouldCheck(activity)) {
            Log.d("AppChecker", "Not time to check yet.");
            return; // Don't check if it hasn't been a week
        }

        // Check for internet connectivity first
        if (!isNetworkAvailable(activity)) {
            // If no internet, schedule a check for when it becomes available
            // This part is tricky.  Android doesn't guarantee exact timing.
            // You could use a JobScheduler or WorkManager for a more robust solution.
            // For simplicity, we'll just set a flag to check next time the app opens.
            Log.d("AppChecker", "No internet, will check next time app opens.");
            return;
        }

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
                Log.e("AppChecker", "Malformed URL Exception: " + e.getMessage());
                disableApp(activity, "Error connecting to server. Please try again later.");
                return; // Exit thread if URL is invalid
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
                Log.e("AppChecker", "IO Exception opening stream: " + e.getMessage());
                disableApp(activity, "Error connecting to server. Please try again later.");
                return; // Exit thread if stream fails
            }
            String input = null;
            StringBuilder stringBuffer = new StringBuilder();
            try {
                while (true) {
                    if (in == null) break; // Check for null input stream
                    input = in.readLine();
                    if (input == null) break;
                    stringBuffer.append(input);
                }
            } catch (final IOException e) {
                e.printStackTrace();
                Log.e("AppChecker", "IO Exception reading line: " + e.getMessage());
                disableApp(activity, "Error connecting to server. Please try again later.");
                return; // Exit thread if reading fails
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    Log.e("AppChecker", "IO Exception closing stream: " + e.getMessage());
                }
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    disableApp(activity, msg);
                } else {
                    enableApp(activity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("AppChecker", "JSON Exception: " + e.getMessage());
                disableApp(activity, "Error verifying license. Please try again later.");
            } finally {
                // Update the last check date regardless of success or failure, but only if the check was successful
                if (isNetworkAvailable(activity)) {
                    updateLastCheckDate(activity);
                }
            }
        }).start();
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static void showAppDisabledDialog(Activity activity) {
        activity.runOnUiThread(() -> {
            new AlertDialog.Builder(activity)
                    .setTitle("App Disabled")
                    .setMessage("This app requires an internet connection at least once a week to verify your license. Please connect to the internet and restart the app.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        activity.finish(); // Close the activity
                    })
                    .show();
        });
    }

    private static boolean shouldCheck(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastCheck = prefs.getLong(LAST_CHECK_DATE, 0);
        Calendar lastCheckCalendar = Calendar.getInstance();
        lastCheckCalendar.setTimeInMillis(lastCheck);

        Calendar now = Calendar.getInstance();

        // Check if it's been at least a week since the last check
        Calendar lastWeek = Calendar.getInstance();
        lastWeek.add(Calendar.DAY_OF_YEAR, -7);

        return lastCheck == 0 || lastCheckCalendar.before(lastWeek);
    }

    private static void updateLastCheckDate(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(LAST_CHECK_DATE, System.currentTimeMillis());
        editor.apply();
        Log.d("AppChecker", "Last check date updated.");
    }

    private static void disableApp(Activity activity, String message) {
        setAppEnabled(activity, false);
        activity.runOnUiThread(() -> {
            new AlertDialog.Builder(activity)
                    .setTitle("App Disabled")
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        activity.finish(); // Close the activity
                    })
                    .show();
        });
    }

    private static void enableApp(Activity activity) {
        setAppEnabled(activity, true);
    }

    private static void setAppEnabled(Context context, boolean enabled) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(APP_ENABLED, enabled);
        editor.apply();
        Log.d("AppChecker", "App enabled state: " + enabled);
    }

    private static boolean isAppEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(APP_ENABLED, true); // Default to true
    }
}
