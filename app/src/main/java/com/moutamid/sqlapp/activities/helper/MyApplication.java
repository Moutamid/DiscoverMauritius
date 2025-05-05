package com.moutamid.sqlapp.activities.helper;

import android.app.Application;

import com.fxn.stash.Stash;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stash.init(this);
        Stash.put("wasRunning", true);

    }
}
