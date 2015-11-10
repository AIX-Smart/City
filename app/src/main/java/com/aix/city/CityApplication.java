package com.aix.city;

import android.app.Application;

import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxLoginModule;

/**
 * Created by Thomas on 02.11.2015.
 */
public class CityApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.
        initSingletons();
    }

    public void initSingletons() {
        AIxNetworkManager.initInstance(this);
        AIxLoginModule.initInstance(this);
        AIxDataManager.initInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}
