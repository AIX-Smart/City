package com.aix.city;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aix.city.core.AIxLoginModule;
import com.aix.city.core.data.Location;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Thomas on 08.02.2016.
 */
public class AuthenticationDialog extends Dialog implements Observer {

    private Location location;
    private Button btnAuthenticate;
    private Button btnCancel;
    private EditText txtMail;
    private EditText txtPassword;
    private ProgressBar loadingPanel;

    public AuthenticationDialog(Context context) {
        super(context);
    }

    public AuthenticationDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public AuthenticationDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.authenticate_dialog);

        btnAuthenticate = (Button) findViewById(R.id.btnAuthenticate);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtMail = (EditText) findViewById(R.id.txtMail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        loadingPanel = (ProgressBar) findViewById(R.id.auth_progress_bar);

        btnAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticate();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        AIxLoginModule.getInstance().addObserver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AIxLoginModule.getInstance().deleteObserver(this);
    }

    public void authenticate(){
        if (location == null){
            throw new IllegalStateException();
        }

        String mail = txtMail.getText().toString();
        String pass = txtPassword.getText().toString();

        if(mail.trim().length() > 0 && pass.trim().length() > 0) {
            AIxLoginModule.getInstance().authenticate(location, mail, pass);
            btnAuthenticate.setVisibility(View.INVISIBLE);
            btnAuthenticate.setEnabled(false);
            loadingPanel.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.authentification_missing_input), Toast.LENGTH_LONG).show();
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        setTitle("Authentifikation zu " + location.getName());
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null){
            switch (data.toString()){
                case AIxLoginModule.OBSERVER_KEY_AUTHENTICATE_SUCCESS:
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.authentification_success), Toast.LENGTH_LONG).show();
                    dismiss();
                    break;
                case AIxLoginModule.OBSERVER_KEY_AUTHENTICATE_INCORRECT_INPUT:
                    txtMail.setText("");
                    txtPassword.setText("");
                    btnAuthenticate.setVisibility(View.VISIBLE);
                    btnAuthenticate.setEnabled(true);
                    loadingPanel.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.authentification_incorrect), Toast.LENGTH_LONG).show();
                    break;
                case AIxLoginModule.OBSERVER_KEY_AUTHENTICATE_FAILURE:
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.connectionError), Toast.LENGTH_LONG).show();
                    dismiss();
                    break;
            }
        }
    }
}
