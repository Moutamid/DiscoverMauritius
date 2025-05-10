package com.moutamid.sqlapp.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.fxn.stash.Stash;

public class MyApplication extends Application {

    private int activityCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyApp", "Application started");

        Stash.init(this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityStarted(Activity activity) {
                activityCount++;

                if (activityCount == 1) {
                    // App just entered foreground
                    Log.d("MyApp", activityCount+"   App came to foreground");
                    Stash.put("appHandledReopen", false);
                    Stash.put("wasRunning", true);

                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                activityCount--;

                if (activityCount == 0) {
                    // App just went to background
                    Log.d("MyApp", "App went to background");

                        Stash.put("wasRunning", true);
                    }
            }

            // Other methods not used
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
            public void onActivityResumed(Activity activity) {}
            public void onActivityPaused(Activity activity) {}
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
            public void onActivityDestroyed(Activity activity) {}
        });
    }
}
