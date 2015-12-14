package com.aix.city;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxLoginModule;
import com.aix.city.core.AIxNetworkManager;
import com.aix.city.core.ListingSource;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.User;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends AppCompatActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AIxNetworkManager.getInstance().init();
        AIxLoginModule.getInstance().addObserver(this);
        AIxLoginModule.getInstance().login();
        AIxDataManager.getInstance().init();
    }

    public void loginFailure() {
        AIxLoginModule.getInstance().setLoggedInUser(new User(1, new ArrayList<Location>(), 0));
        Toast.makeText(this, this.getResources().getString(R.string.loginFailure), Toast.LENGTH_SHORT).show();
        finishLoginActivity();
    }

    public void loginSuccess(){
        finishLoginActivity();
    }

    public void finishLoginActivity(){
        AIxLoginModule.getInstance().deleteObserver(this);

        Intent intent = new Intent(this, BaseListingActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null) {
            switch (data.toString()) {
                case AIxLoginModule.OBSERVER_KEY_LOGIN_SUCCESS:
                    loginSuccess();
                    break;
                case AIxLoginModule.OBSERVER_KEY_LOGIN_FAILURE:
                    loginFailure();
                    break;
            }
        }
    }
}
