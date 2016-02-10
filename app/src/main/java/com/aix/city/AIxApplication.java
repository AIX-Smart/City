package com.aix.city;

import android.app.Application;
import android.content.Context;

import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxLoginModule;

/**
 * Created by Thomas on 02.11.2015.
 */
public class AIxApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        // Initialize the singletons so their instances
        // are bound to the application process.
        AIxNetworkManager.createInstance(this);
        AIxLoginModule.createInstance(this);
        AIxDataManager.createInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getContext(){
        return mContext;
    }
}
