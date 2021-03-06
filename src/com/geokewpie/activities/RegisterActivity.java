package com.geokewpie.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.geokewpie.R;
import com.geokewpie.beans.Authorization;
import com.geokewpie.content.Properties;
import com.geokewpie.network.Response;
import com.geokewpie.tasks.RegisterUserTask;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

public class RegisterActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set View to register.xml
        setContentView(R.layout.register);

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Switching to Login Screen
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
    }

    public void register(View view) {
        EditText userText = (EditText) findViewById(R.id.reg_user);
        EditText emailText = (EditText) findViewById(R.id.reg_email);
        EditText passwordText = (EditText) findViewById(R.id.reg_password);

        RegisterUserTask rut = new RegisterUserTask(getApplicationContext());
        rut.execute(userText.getText().toString(), emailText.getText().toString(), passwordText.getText().toString());

        try {
            Response registerResponse = rut.get();
            System.out.println("registerUserResponse = " + registerResponse);

            if (registerResponse != null && registerResponse.isSuccessful()) {
                Authorization auth = new Gson().fromJson(registerResponse.getBody(), Authorization.class);

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(Properties.EMAIL, emailText.getText().toString());
                editor.putString(Properties.LOGIN, userText.getText().toString());
                editor.putString(Properties.AUTH_TOKEN, auth.getAuth_token());
                editor.putString(Properties.REFRESH_TOKEN, auth.getRefresh_token());
                editor.apply();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            } else {
                // todo implement
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


}