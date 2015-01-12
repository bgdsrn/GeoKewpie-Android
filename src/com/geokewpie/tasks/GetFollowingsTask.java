package com.geokewpie.tasks;

import android.os.AsyncTask;
import com.geokewpie.beans.UserLocation;
import com.geokewpie.network.NetworkTools;
import com.geokewpie.network.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GetFollowingsTask extends AsyncTask<String, Void, List<UserLocation>> {
    @Override
    protected List<UserLocation> doInBackground(String... strings) {

        try {
            String email = strings[0];
            String authToken = strings[1];

            String url = MessageFormat.format("https://198.199.109.47:8080/locations?email={0}&auth_token={1}", email, authToken);

            Response response = NetworkTools.sendGet(url);

            if (response.isSuccessful()) {
                Type listType = new TypeToken<ArrayList<UserLocation>>() {
                }.getType();

                return new Gson().fromJson(response.getBody(), listType);
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
