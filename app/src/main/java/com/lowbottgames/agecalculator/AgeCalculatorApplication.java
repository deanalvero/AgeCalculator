package com.lowbottgames.agecalculator;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AgeCalculatorApplication extends Application {

//    private static final String TAG = AgeCalculatorApplication.class.getSimpleName();
    public DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
    }

    private void setupDatabase() {
//        Log.e(TAG, "setupDatabase()");

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "agecalculator-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
