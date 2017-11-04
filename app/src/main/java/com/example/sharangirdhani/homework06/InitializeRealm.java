package com.example.sharangirdhani.homework06;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sharangirdhani on 11/4/17.
 */

public class InitializeRealm extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.getInstance(config);
    }
}