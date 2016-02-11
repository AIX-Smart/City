package com.aix.city;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.aix.city.core.AIxDataManager;
import com.aix.city.core.AIxLoginModule;
import com.aix.city.core.AIxNetworkManager;

import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends AppCompatActivity implements Observer {

    //timer delay for a retry after a failed login in milliseconds
    private static final int LOGIN_RETRY_DELAY_MS = 1000;

    private final Handler retryHandler = new Handler();
    private final Runnable retryRunnable = new Runnable() {
        @Override
        public void run() {
            AIxLoginModule.getInstance().login();
        }
    };

    private boolean loginFailed = false;
    private Toast loginFailureToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AIxNetworkManager.getInstance().start();
    }

    @Override
    protected void onStart() {
        AIxLoginModule.getInstance().addObserver(this);

        AIxLoginModule.getInstance().login();
        AIxDataManager.getInstance().init();

        super.onStart();
    }

    @Override
    protected void onStop() {
        AIxLoginModule.getInstance().deleteObserver(this);
        retryHandler.removeCallbacks(retryRunnable);
        super.onStop();
    }

    public void loginFailure() {
        retryHandler.postDelayed(retryRunnable, LOGIN_RETRY_DELAY_MS);

        if (!loginFailed){
            loginFailureToast = Toast.makeText(this, this.getResources().getString(R.string.connectionError), Toast.LENGTH_LONG);
            loginFailureToast.show();
            loginFailed = true;
        }
    }

    public void loginSuccess(){
        if (loginFailureToast != null){
            loginFailureToast.cancel();
        }
        finishLoginActivity();
    }

    public void finishLoginActivity(){
        Intent intent = new Intent(this, AIxMainActivity.class);
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
