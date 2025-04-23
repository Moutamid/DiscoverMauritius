package com.moutamid.sqlapp.helper;

import android.app.Application;
import android.content.SharedPreferences;

import com.fxn.stash.Stash;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stash.init(this);
        Stash.put("wasRunning", true);

    }
}
