package com.aix.city;

import android.app.Application;

import com.aix.city.comm.AIxNetworkManager;
import com.aix.city.core.AIXLoginModule;
import com.aix.city.core.AIxDataManager;

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
        AIXLoginModule.initInstance(this);
        AIxDataManager.initInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}
